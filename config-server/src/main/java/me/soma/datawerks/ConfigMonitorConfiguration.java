package me.soma.datawerks;

import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Created by somasundar.sekar on 3/10/2017.
 */
@Profile("config-monitor")
@Configuration
@Import(RabbitAutoConfiguration.class)
public class ConfigMonitorConfiguration {

}
