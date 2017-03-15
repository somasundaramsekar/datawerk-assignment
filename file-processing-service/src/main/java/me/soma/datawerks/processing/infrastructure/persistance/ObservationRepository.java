package me.soma.datawerks.processing.infrastructure.persistance;

import me.soma.datawerks.processing.domain.models.Observation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ObservationRepository  extends JpaRepository<Observation, Integer>{

}
