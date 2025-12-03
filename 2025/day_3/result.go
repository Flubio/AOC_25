package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	resultPart1 := solPart1(0)
	fmt.Printf("Part 1: %d\n", resultPart1)
	fmt.Printf("Part 2: %d\n", solPart2())
}

func solPart1(numBatteries int) int {
	// Find the maximum joltage from battery banks by selecting the two highest digits from each line.
	// Each line represents a bank of batteries with joltage ratings 1-9.
	// The joltage produced is formed by concatenating the two highest digits (maintaining position order).
	// Return the sum of maximum joltages from all banks.

	input := readInput()
	lines := strings.Split(strings.TrimSpace(input), "\n")
	totalJoltage := 0
	if numBatteries < 1 {
		numBatteries = 2
	}

	for _, line := range lines {
		digits := []int{}
		for _, ch := range line {
			digits = append(digits, int(ch-'0'))
		}
		bankLen := len(digits)
		batteriesRemaining := numBatteries
		batteriesEnabled := []int{}
		bankIndex := -1

		for batteriesRemaining > 0 {
			// Find max digit in valid slice (leaving room for remaining batteries)
			searchStart := bankIndex + 1
			searchEnd := bankLen - batteriesRemaining + 1
			maxVal := -1
			maxIdx := -1
			for i := searchStart; i < searchEnd; i++ {
				if digits[i] > maxVal {
					maxVal = digits[i]
					maxIdx = i
				}
			}
			bankIndex = maxIdx
			batteriesEnabled = append(batteriesEnabled, maxVal)
			batteriesRemaining--
		}

		// Concatenate selected digits to form joltage
		joltage := 0
		for _, d := range batteriesEnabled {
			joltage = joltage*10 + d
		}
		totalJoltage += joltage
	}

	return totalJoltage
}

func solPart2() int {
	// For part 2, select the top 12 batteries from each bank
	return solPart1(12)
}

func readInput() string {
	// Read input from file
	data, err := os.ReadFile("input/input.txt")
	if err != nil {
		fmt.Println("Error reading file:", err)
		return ""
	}
	return string(data)
}
