package com.ragbecca.rgbplayerservice.repository;

import com.ragbecca.rgbplayerservice.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
