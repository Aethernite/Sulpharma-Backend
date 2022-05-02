package com.university.sofia.sulpharma.business;

import com.university.sofia.sulpharma.persistence.dto.LocationDTO;
import com.university.sofia.sulpharma.persistence.entity.Location;
import com.university.sofia.sulpharma.persistence.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Location service.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    private static final String LOCATION_NOT_FOUND = "Location with id %s is not found";


    /**
     * Create location.
     *
     * @param locationDTO the location dto
     * @return the location dto
     */
    public LocationDTO createLocation(LocationDTO locationDTO) {
        Location location = modelMapper.map(locationDTO, Location.class);
        location = locationRepository.save(location);
        log.info("Created location with id {}", location.getId());
        return modelMapper.map(location, LocationDTO.class);
    }

    /**
     * Find all locations not paginated.
     *
     * @return the list
     */
    public List<LocationDTO> findAll() {
        log.info("Fetch all locations");
        return locationRepository.findAll()
                .stream()
                .map(location -> modelMapper.map(location, LocationDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Gets location by id.
     *
     * @param locationId the location id
     * @return the location by id
     */
    public LocationDTO getLocationById(Long locationId) {
        log.info("Get Location by id: {}", locationId);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LOCATION_NOT_FOUND, locationId)));
        return modelMapper.map(location, LocationDTO.class);
    }

    /**
     * Update location by id.
     *
     * @param locationId         the location id
     * @param updatedLocationDTO the updated location dto
     * @return the location dto
     */
    public LocationDTO updateLocationById(Long locationId, LocationDTO updatedLocationDTO) {
        log.info("Start updating location with id: {}", locationId);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LOCATION_NOT_FOUND, locationId)));

        location.update(modelMapper.map(updatedLocationDTO, Location.class));

        locationRepository.save(location);
        log.info("Updated location with id: {}", locationId);

        return modelMapper.map(location, LocationDTO.class);
    }

    /**
     * Delete location by id.
     *
     * @param locationId the location id
     * @return the deleted location dto
     */
    public LocationDTO deleteLocationById(Long locationId) {
        log.info("Start deleting location with id {}", locationId);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LOCATION_NOT_FOUND, locationId)));
        locationRepository.delete(location);
        log.info("Deleted location with id {}", locationId);
        return modelMapper.map(location, LocationDTO.class);
    }
}