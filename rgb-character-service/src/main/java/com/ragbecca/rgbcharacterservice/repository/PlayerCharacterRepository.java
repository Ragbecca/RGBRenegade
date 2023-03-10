package com.ragbecca.rgbcharacterservice.repository;

import com.ragbecca.rgbcharacterservice.model.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long> {
    PlayerCharacter findByUsername(String username);

    List<PlayerCharacter> findAllByAccepted(boolean accepted);

    void deleteByUsername(String username);
}
