package com.osoc.oncera;

public final class Evaluator {

    static public boolean IsGreaterThan(float to_evaluate, float other)
    {
        return to_evaluate > other;
    }

    static public boolean IsLowerThan (float to_evaluate, float other)
    {
        return to_evaluate < other;
    }

    static public boolean IsInRange(float to_evaluate, float min, float max)
    {
        return IsGreaterThan(to_evaluate, min) && IsLowerThan(to_evaluate, max);
    }

    static public boolean IsEqualsTo(float to_evaluate, float other)
    {
        return to_evaluate == other;
    }

    static public boolean IsEqualsTo(boolean to_evaluate, boolean other)
    {
        return to_evaluate == other;
    }


}
