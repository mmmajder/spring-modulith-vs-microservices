package org.example.gym.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gym.application.GymDto;
import org.example.gym.application.GymService;
import org.example.gym.domain.Gym;
import org.example.gym.ui.dto.CreateGymRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workout-service/gyms")
@RequiredArgsConstructor
public class GymController {
    private final GymService gymService;

    @GetMapping("/closest")
    public ResponseEntity<Page<GymDto>> getClosestGyms(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<GymDto> gyms = gymService.getClosestGyms(latitude, longitude, radius, pageable);
        return ResponseEntity.ok(gyms);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GymDto>> getCitiesByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<GymDto> cities = gymService.getCitiesOrGymsByName(name, pageable);
        return ResponseEntity.ok(cities);
    }

    @GetMapping
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Bravoo");
    }

    @PostMapping
    public ResponseEntity<GymDto> createGym(@Valid @RequestBody CreateGymRequest request) {
        Gym.Address address = new Gym.Address(request.getStreet(), request.getCity(), request.getCountry(), request.getNumber(), request.getLatitude(), request.getLongitude());
        GymDto gym = gymService.createGym(request.getName(), address);
        return ResponseEntity.status(HttpStatus.CREATED).body(gym);
    }
}
