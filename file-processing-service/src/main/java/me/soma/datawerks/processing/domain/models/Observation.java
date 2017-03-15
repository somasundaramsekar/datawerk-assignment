package me.soma.datawerks.processing.domain.models;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Access(AccessType.FIELD)
public class Observation {

	private static final ZoneId zone = ZoneId.of("Europe/Berlin");
	private static final DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("d-M-yyyy HH:mm:ss").withZone(zone);

	private static final Pattern twoDigitDatePattern = Pattern.compile("-\\d{2}\\s");

	@Id
	public Integer id;

	public String name;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timeOfStart;

	Observation() {
		// TODO Auto-generated constructor stub
	}

	public Observation(Integer id, String name, String timeOfStart) {
		super();
		this.id = id;
		this.name = name;
		setTimeOfStart(timeOfStart);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getTimeOfStart() {
		return timeOfStart;
	}

	public void setTimeOfStart(String timeOfStart) {
		try {
			this.timeOfStart = convertToZonedDataTime.apply(timeOfStart);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException("Invalid date format specified");
		}
	}

	@Transient
	Function<String, Date> convertToZonedDataTime = (String input) -> {
		Matcher m = twoDigitDatePattern.matcher(input);
		String formattedDate = input;
		while (m.find()) {
			formattedDate = input.replaceAll(twoDigitDatePattern.pattern(), "-19"+m.group().substring(1));
		}

		ZonedDateTime utc = ZonedDateTime.parse(formattedDate, formatter)
				.withZoneSameInstant(ZoneOffset.UTC);
		return Date.from(utc.toInstant());
	};

	@Override
	public String toString() {
		return "Observation{" +
				"id=" + id +
				", name='" + name + '\'' +
				", timeOfStart=" + timeOfStart +
				'}';
	}
}
