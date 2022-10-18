package com.example.aitools;

// This class is used to store the variables that will later be sent to the API.
public abstract class Options {

    // Level of AI improvisation (0 to 1)
    public static float temperature = 0;

    // Maximum number of characters to be returned by the API
    public static int max_tokens = 100;
}
