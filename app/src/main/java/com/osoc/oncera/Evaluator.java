package com.osoc.oncera;

/**
 * Class to compare values
 */
public final class Evaluator {

    /**
     * @param to_evaluate param to check if it is greater than
     * @param other value to compare against to_evaluate
     * @return true if to_evaluate is greater than other
     */
    static public boolean IsGreaterThan(float to_evaluate, float other)
    {
        return to_evaluate > other;
    }

    /**
     * @param to_evaluate param to check if it is lower than
     * @param other value to compare against to_evaluate
     * @return true if to_evaluate is lower than other
     */
    static public boolean IsLowerThan (float to_evaluate, float other)
    {
        return to_evaluate < other;
    }

    /**
     * Check if a value is between a range
     * @param to_evaluate value to check
     * @param min lower value (exclusive)
     * @param max higher value (exclusive)
     * @return true if to_evaluate is in the range
     */
    static public boolean IsInRange(float to_evaluate, float min, float max)
    {
        return IsGreaterThan(to_evaluate, min) && IsLowerThan(to_evaluate, max);
    }

    /**
     *
     * @param to_evaluate
     * @param other
     * @return true if to_evaluate is equal to other
     */
    static public boolean IsEqualsTo(boolean to_evaluate, boolean other)
    {
        return to_evaluate == other;
    }

}
