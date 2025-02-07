package com.faforever.client.mapstruct;

import com.faforever.commons.api.dto.CoturnServer;
import org.mapstruct.Mapper;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper(config = MapperConfiguration.class)
public abstract class IceServerMapper {
  public Map<String, Object> map(CoturnServer coturnServer) {
    return Map.of(
        "urls", coturnServer.getUrls().stream().map(URI::toString).toList(),
        "credential", coturnServer.getCredential(),
        "username", coturnServer.getUsername()
    );
  }

  public abstract List<Map<String, Object>> map(Collection<CoturnServer> coturnServers);

}
