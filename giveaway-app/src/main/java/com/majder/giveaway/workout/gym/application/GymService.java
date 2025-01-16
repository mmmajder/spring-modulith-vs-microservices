package com.majder.giveaway.workout.gym.application;

import com.majder.giveaway.workout.WorkoutPerformed;
import com.majder.giveaway.workout.gym.domain.Gym;
import com.majder.giveaway.workout.gym.domain.GymRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class GymService {
    private final GymRepository gymRepository;
    private final GymMapper mapper;

    @ApplicationModuleListener
    public void on(WorkoutPerformed event) {
        Gym gym = gymRepository.findById(event.gymId()).orElseThrow(() -> new GymNotFoundException("Gym for given id does not exist."));
        gym.increaseNumberOfVisits();
        gymRepository.save(gym);
    }

    public Page<GymDto> getClosestGyms(double latitude, double longitude, double radius, Pageable pageable) {
        return gymRepository.findClosestGyms(latitude, longitude, radius, pageable).map(mapper::toDto);
    }

    public Page<GymDto> getCitiesOrGymsByName(String name, Pageable pageable) {
        return gymRepository.findGymsByCityNameOrGymName(name, pageable).map(mapper::toDto);
    }

    public GymDto createGym(String name, Gym.Address address) {
        Gym gym = new Gym(name, address);
        Gym savedGym = gymRepository.save(gym);

        return mapper.toDto(savedGym);
    }
}