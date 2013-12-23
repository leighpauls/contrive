package com.leighpauls.unwpi;

/**
 * Master switch for controlling a real robot or a simulation
 */
public class InjectionController {
    public static boolean isEmulation() {
        return true;
    }
}
