using UnityEngine;
using System.Collections;

/// <summary>
/// 1 dimensional Numerical zero-finder
/// </summary>
public class NumericalSolver {
	/**
	 * Input function functor interface
	 */
	public interface FunctionToSolve {
		float function(float x);
	}

	static int depth = 0;

	public static float Solve(FunctionToSolve f, float lowerBound, float upperBound, int numDivisions, float accuracy) {
		float prevInput = lowerBound;
		float prevOutput = f.function(prevInput);
		for (int i = 1; i <= numDivisions; i++) {
			float nextInput = lowerBound + ((upperBound - lowerBound) / numDivisions) * i;
			float nextOutput = f.function(nextInput);
			if (prevOutput * nextOutput > 0f) {
				// didn't cross zero
				prevInput = nextInput;
				prevOutput = nextOutput;
				continue;
			}
			// crossed zero, is it accurate enough?
			if (Mathf.Abs(prevOutput) < accuracy || Mathf.Abs(nextOutput) < accuracy) {
				// accurate enough, return the weighted midpoint
				depth = 0;
				return (prevInput * nextOutput + nextInput * prevOutput) / (nextOutput + prevOutput);
			}
			// crossed zero, but not with enough accuracy
			depth++;
			return Solve(f, prevInput, nextInput, numDivisions, accuracy);
		}
		int d = depth;
		depth = 0;
		throw new UnityException("Couldn't find a zero: " + d);
	}
}
