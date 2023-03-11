package domain.service;

import java.time.ZoneId;

public class TimeZoneResolverImpl implements TimeZoneResolver {

    public ZoneId getTimeZoneForCity(String cityName) {
        return ZoneId.getAvailableZoneIds().stream()
                .map(ZoneId::of)
                .filter(
                        zoneId -> zoneId
                                .toString()
                                .toLowerCase()
                                .contains(cityName.toLowerCase())
                )
                .findFirst()
                .orElseThrow();
    }

}
