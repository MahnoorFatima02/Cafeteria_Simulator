# Simulation Engine Project

## Overview

This project is a simulation engine designed to model and analyze the behavior of a cafeteria system
with multiple service points, including vegan and non-vegan food stations, cashier service points,
and self-checkout stations. The simulation engine is built using Java and includes various components
to handle events, manage queues, and adjust simulation parameters dynamically.

## Features

- **Event Handling**: Processes arrival and departure events to simulate customer flow through the
    service points.
- **Queue Management**: Manages queues at different service points and assigns customers based on queue
    length or customer preference.
- **Dynamic Adjustments**: Adjusts simulation parameters such as service speeds and arrival rates based
    on predefined flags.
- **Data Visualization**: Uses JFreeChart and SwingX libraries to visualize simulation metrics and statistics.
- **Animation**: Provides real-time animation of the simulation process.
- **Unit Testing**: Comprehensive unit tests using JUnit and Mockito to ensure the correctness of the
    simulation engine.

## Project Structure

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven 3.6.0 or higher
- MySQL 8.0 or higher
- JUnit 5 and Mockito

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/MahnoorFatima02/simulation-engine.git
   cd simulation-engine


# Running the Simulation
To run the simulation, execute the SimulationView class from your IDE or use the following Maven command:
```mvn exec:java -Dexec.mainClass="simu.view.SimulationView"```

Or you can start the ```Main``` file from the view folder.

# Running Tests
To run the unit tests, use the following Maven command:
```mvn test```
Or run the ```MyEngineTest``` file from the Test folder.

## Usage
## Adjusting Simulation Parameters
You can adjust the simulation parameters by modifying the SimulationVariables and SimulationAdjustments
classes.
These parameters control the arrival rates, service speeds, and other aspects of the simulation.

## Visualizing Data
The project includes examples of how to use JFreeChart and SwingX libraries to visualize simulation data.
Refer to the MyEngine class for examples of generating and updating charts.

## Animation
The simulation includes real-time animation to visualize the flow of customers through the service points.
 The SimulationView class handles the animation and updates the display as the simulation progresses.