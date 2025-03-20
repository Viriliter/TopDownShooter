# TopDownShooter

## Overview
TopDownShooter is a top down shooter game where the players try to survive from zombie attacks as much as they can.

## Features
- Multiple levels with increasing difficulty
- Various zombie types with unique behaviors
- Different weapons to defend against zombie attacks
- Retro-style graphics and sound effects

## Requirements
- Java 21 or above
- Gradle 7.0+
- ini4j
- Windows (not mandatory but need comprehensive tests for other OSes)

## Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/Viriliter/TopDownShooter.git
    ```
2. Navigate to the project directory:
    ```sh
    cd TopDownShooter
    ```
3. Build the project using Gradle Wrapper.
    ```sh
    ./gradlew.bat build
    ```
4. Run the project using Gradle Wrapper.
    ```sh
    ./gradlew.bat run
    ```

## How to Play
- Use the arrow keys or WASD to move your character.
- Use the mouse to aim and shoot.
- Collect power-ups and ammo to gain advantages.
- Defeat all enemies to progress to the next level.

## Game Configurations and Benchmarking
The [config.ini](app/src/main/resources/config.ini) file is a key component of this game, allowing users to customize gameplay elements, player attributes, zombie characteristics, and level properties. It also serves as a useful tool for benchmarking the game engine’s performance by modifying different parameters.

### Sections and Parameters

1. Window Settings

    Defines the game window dimensions and initial position:

    `Width`: Window width (e.g., 1600)

    `Height`: Window height (e.g., 1000)

    `X`, `Y`: Starting position of the window on the screen

2. Weapon Configurations
    Each weapon has the following properties:

    `Damage`: Amount of damage dealt per shot

    `FireRate`: Rate of fire (shots per minute)

    `MagazineCapacity`: Bullets per magazine

    `MagazineCount`: Number of available magazines (-1 for unlimited)

    `ReloadDuration`: Time required to reload (currently 0)

    Available weapons:

    `Pistol`: Balanced starter weapon

    `Assault Rifle`: High fire rate, good damage output

    `Shotgun`: Slow fire rate, useful in close combat

    `Sniper Rifle`: High damage, long-range weapon

    `Rocket Launcher`: Heavy damage, slow fire rate

3. Player Properties

    Defines the player's initial attributes:

    `StartingX`, `StartingY`: Initial player position

    `StartingHealth`: Player’s starting health (default 100)

    `Speed`: Movement speed

4. Zombie Properties

    Each zombie has the following properties:

    `Health`: Amount of health points

    `Speed`: Movement speed

    `Damage`: Attack damage

    `Points`: Points awarded for defeating the enemy

    Enemy types:

    `Ordinary Zombie`: Basic enemy with moderate health and speed

    `Crawler Zombie`: Faster but weaker than ordinary zombies

    `Tank Zombie`: Slow but extremely durable and strong

    `Acid Zombie`: Deals high damage with moderate speed

5. Level Configurations

    Each level has the following properties:

    `WaveDuration`: Duration of the wave (in seconds)

    `OrdinaryZombieCount`: Number of ordinary zombies

    `CrawlerZombieCount`: Number of crawler zombies

    `TankZombieCount`: Number of tank zombies

    `AcidZombieCount`: Number of acid zombies

    `WeaponPrize (optional)`: Unlockable weapon reward

### Benchmarking and Performance Testing

Adjust [config.ini](app/src/main/resources/config.ini) to test the game engine’s performance. 

Key settings may be modified for performance testing:

* Increase zombie counts to stress game physics and rendering (e.g `OrdinaryZombieCount` and `CrawlerZombieCount`)

* Adjust `WaveDuration` to see performance over extended play sessions.

* Change window size (`Width` & `Height`) to evaluate rendering efficiency.

To run a benchmark, modify config.ini, save changes, and restart the game

## Contributing
N/A

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
For any inquiries or feedback, please contact mert.lim07@gmail.com.
