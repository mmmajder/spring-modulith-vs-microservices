package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPerformedEvent {
    private String userEmail;
    private Long minutes;
    private Long gymId;
}