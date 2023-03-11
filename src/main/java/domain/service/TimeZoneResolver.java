package domain.service;

import java.time.ZoneId;

public interface TimeZoneResolver {

    ZoneId getTimeZoneForCity(String cityName);
}
