package domain.service;

import java.time.ZoneId;

public class TimeZoneResolverImpl implements TimeZoneResolver {

    public ZoneId getTimeZoneForCity(String cityName) {
        // cityName is ignored because of that
        // afterward the company declared that all the time is of the same time zone
        final String standardCitiName = "Vladivostok";
        return ZoneId.getAvailableZoneIds().stream()
                .map(ZoneId::of)
                .filter(
                        zoneId -> zoneId
                                .toString()
                                .toLowerCase()
                                .contains(standardCitiName.toLowerCase())
                )
                .findFirst()
                .orElseThrow();
    }

}
