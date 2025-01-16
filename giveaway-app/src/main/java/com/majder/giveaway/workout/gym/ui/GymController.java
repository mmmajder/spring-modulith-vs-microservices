package com.majder.giveaway.workout.gym.ui;

import com.majder.giveaway.utils.annotation.Log;
import com.majder.giveaway.workout.gym.application.GymDto;
import com.majder.giveaway.workout.gym.application.GymService;
import com.majder.giveaway.workout.gym.domain.Gym;
import com.majder.giveaway.workout.gym.ui.dto.CreateGymRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gyms")
@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;

    @GetMapping("/closest")
    @Log(message = "Fetching the closest gyms based on provided location")
    public ResponseEntity<Page<GymDto>> getClosestGyms(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<GymDto> gyms = gymService.getClosestGyms(latitude, longitude, radius, pageable);
        if (gyms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(gyms);
    }

    @GetMapping("/search")
    @Log(message = "Searching gyms by name or city")
    public ResponseEntity<Page<GymDto>> getGymsByGymOrCityName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<GymDto> gyms = gymService.getCitiesOrGymsByName(name, pageable);
        if (gyms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(gyms);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Log(message = "Creating a new gym with provided details")
    public ResponseEntity<GymDto> createGym(@Valid @RequestBody CreateGymRequest request) {
        Gym.Address address = new Gym.Address(
                request.getStreet(),
                request.getCity(),
                request.getCountry(),
                request.getNumber(),
                request.getLatitude(),
                request.getLongitude()
        );

        GymDto gym = gymService.createGym(request.getName(), address);
        return ResponseEntity.status(HttpStatus.CREATED).body(gym);
    }

    @ExceptionHandler(Exception.class)
    @Log(message = "An error occurred in GymController")
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + ex.getMessage());
    }
}
