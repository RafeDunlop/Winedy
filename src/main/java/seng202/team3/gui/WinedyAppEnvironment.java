package seng202.team3.gui;

import java.util.function.Consumer;

/**
 * WinedyAppEnvironment class that controls the flow of the app and holds data of important variables
 * @author Krishna Sridhar (nsr36)
 */

public class WinedyAppEnvironment {
    private final Consumer<WinedyAppEnvironment> homeScreenLauncher;
    private final Consumer<WinedyAppEnvironment> navBarLauncher;
    private final Consumer<WinedyAppEnvironment> dummy1Launcher;
    private final Consumer<WinedyAppEnvironment> dummy2Launcher;
    private final Runnable clearScreen;
    private final Runnable clearSuper;

    /**
     * Constructor for WinedyAppEnvironment
     * Creates a WinedyAppEnvironment Instance and initialises the launchers of all screens
     *
     * @param homeScreenLauncher used to launch the home screen
     * @param navBarLauncher used to launch the navigation bar
     * @param dummy1Launcher used to launch the dummy1 screen
     * @param dummy2Launcher used to launch the dummy2 screen
     * @param clearScreen used to clear the current screen
     * @param clearSuper used to clear the higher level screen
     */
    public WinedyAppEnvironment(Consumer<WinedyAppEnvironment> homeScreenLauncher,
                                Consumer<WinedyAppEnvironment> navBarLauncher,
                                Consumer<WinedyAppEnvironment> dummy1Launcher,
                                Consumer<WinedyAppEnvironment> dummy2Launcher,
                                Runnable clearScreen,
                                Runnable clearSuper) {
        this.homeScreenLauncher = homeScreenLauncher;
        this.navBarLauncher = navBarLauncher;
        this.dummy1Launcher = dummy1Launcher;
        this.dummy2Launcher = dummy2Launcher;
        this.clearScreen = clearScreen;
        this.clearSuper = clearSuper;
        launchHomeScreen();
    }

    /**
     * Open the home screen
     */
    public void launchHomeScreen() {
        clearSuper.run();
        homeScreenLauncher.accept(this);
    }

    /**
     * Display the navigation bar
     */
    public void launchNavBar() {
        clearSuper.run();
        navBarLauncher.accept(this);
    }

    /**
     * Open the dummy1 screen
     */
    public void launchDummy1() {
        clearScreen.run();
        dummy1Launcher.accept(this);
    }

    /**
     * Open the dummy2 screen
     */
    public void launchDummy2() {
        clearScreen.run();
        dummy2Launcher.accept(this);
    }
}
