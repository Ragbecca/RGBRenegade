package com.ragbecca.rgbcharacterservice.repository;

import com.ragbecca.rgbcharacterservice.model.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {
}
