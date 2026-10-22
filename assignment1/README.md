Assignment 1

## A. Project Overview

**Purpose:** implement four divide and conquer algorithms in Java, analyse their recurrences (Master Theorem, Akra–Bazzi) and compare theory with measured performance (time, recursion depth, comparisons).

| Algorithm | Class | Time |
|---|---|---|
| MergeSort | `MergeSorter` | Θ(n log n) |
| QuickSort | `QuickSorter` | Θ(n log n) expected, O(n²) worst |
| Deterministic Select (Median-of-Medians) | `DeterministicSelector` | Θ(n) worst |
| Closest Pair of Points | `ClosestPairSolver` | Θ(n log n) |

Other files: `Point`, `Main` (demo of all four), `Experiment` (writes `results/results.csv`), tests in `tests/`.
Run: `javac -d bin src/*.java tests/*.java`, then `java -cp bin Main` / `Experiment` / `<Name>Test` (PowerShell: `javac -d bin (Get-ChildItem src,tests -Filter *.java).FullName`).

**AI assistance disclosure.**

An AI assistant (Claude) was used to help with configuring troubles with VS Code, such as merging VS Code with GitHub correctly, explaining theory and checking for mistakes in code and improvements, and writing README report.


## B. Algorithm Analysis

### MergeSort
- **How it works:** split in half, sort both halves recursively, merge with a linear two-pointer pass. One auxiliary buffer is allocated once and reused; subarrays of ≤ 16 elements are sorted by Insertion Sort (cut-off).
- **Time / space:** Θ(n log n) in all cases; Θ(n) extra space + O(log n) stack.
- **Recurrence:** `T(n) = 2T(n/2) + Θ(n)`. Master Theorem: a = 2, b = 2, f(n) = Θ(n) = Θ(n^(log₂2)) → Case 2 (balanced) → **Θ(n log n)**.

### QuickSort
- **How it works:** random pivot value; in-place three-way partition (`<`, `=`, `>` pivot); recurse into the **smaller** outer part, loop over the larger one.
- **Time / space:** expected Θ(n log n), worst case O(n²); O(log n) stack (guaranteed by smaller-first recursion), in place.
- **Recurrence:** `T(n) = T(αn) + T((1−α)n) + Θ(n)`. The parts are unequal, so Akra–Bazzi: `α^p + (1−α)^p = 1` ⇒ p = 1, g(n) = n = Θ(n^p) ⇒ **Θ(n log n)**. Worst case (pivot is always min/max): `T(n) = T(n−1) + Θ(n)` ⇒ **Θ(n²)**.

### Deterministic Select (Median-of-Medians)
- **How it works:** split into groups of 5, take each group's median, find the median of the medians recursively and use it as pivot, partition in place (three-way), recurse **only into the part containing k**.
- **Time / space:** Θ(n) worst case; O(n) extra (copy of the input and arrays of group medians) + O(log n) stack.
- **Recurrence:** `T(n) ≤ T(n/5) + T(7n/10) + Θ(n)` (the pivot discards ≥ 3n/10 elements). Akra–Bazzi: `(1/5)^p + (7/10)^p = 1` ⇒ p ≈ 0.84 < 1, g(n) = n dominates ⇒ **Θ(n)** (equivalently 1/5 + 7/10 = 9/10 < 1: work per level shrinks geometrically).

### Closest Pair of Points
- **How it works:** sort points by x (and once by y); split the x-array in half and split the y-array into the same halves keeping y-order; δ = min of the two half results; build the strip of points closer than δ to the dividing line and compare each strip point only with following points whose y-difference is < δ (a constant number). ≤ 3 points: brute force. Returns the minimum distance.
- **Time / space:** Θ(n log n); Θ(n) extra.
- **Recurrence:** `T(n) = 2T(n/2) + Θ(n)` (linear combine step) ⇒ Master Theorem Case 2 ⇒ **Θ(n log n)**.

---

## C. Experimental Results

**Execution time (ms), random input**

| n | MergeSort | QuickSort | Det. Select | Closest Pair |
|---|---|---|---|---|
| 1,000 | 0.029 | 0.056 | 0.024 | 0.911 |
| 10,000 | 0.542 | 0.608 | 0.285 | 12.362 |
| 50,000 | — | — | — | 44.965 |
| 100,000 | 7.044 | 7.269 | 2.662 | 79.634 |
| 1,000,000 | 84.721 | 81.039 | 28.057 | — |

**Execution time (ms) by input type**

| input | MergeSort (n=1M) | QuickSort (n=1M) | Det. Select (n=1M) | Closest Pair (n=100k) |
|---|---|---|---|---|
| random | 84.721 | 81.039 | 28.057 | 79.634 |
| sorted | 23.598 | 48.446 | 8.054 | 59.892 |
| reverse | 27.706 | 50.489 | 9.410 | 50.842 |
| duplicates | 41.520 | 12.185 | 11.579 | 58.171 |

**Maximum recursion depth, random input**

| n | MergeSort | QuickSort | Det. Select | Closest Pair |
|---|---|---|---|---|
| 1,000 | 6 | 5 | 9 | 9 |
| 10,000 | 10 | 8 | 12 | 12 |
| 50,000 | — | — | — | 15 |
| 100,000 | 13 | 10 | 16 | 16 |
| 1,000,000 | 16 | 12 | 19 | — |

**Maximum recursion depth by input type**

| input | MergeSort (n=1M) | QuickSort (n=1M) | Det. Select (n=1M) | Closest Pair (n=100k) |
|---|---|---|---|---|
| random | 16 | 12 | 19 | 16 |
| sorted | 16 | 12 | 18 | 16 |
| reverse | 16 | 12 | 18 | 16 |
| duplicates | 16 | 1 | 9 | 16 |

**Comparisons, random input**

| n | MergeSort | QuickSort | Det. Select | Closest Pair |
|---|---|---|---|---|
| 1,000 | 10,473 | 17,035 | 9,076 | 1,141 |
| 10,000 | 127,534 | 246,164 | 97,238 | 12,992 |
| 50,000 | — | — | — | 71,071 |
| 100,000 | 1,639,269 | 3,101,092 | 993,614 | 142,727 |
| 1,000,000 | 20,224,235 | 37,465,795 | 10,103,703 | — |

**Comparisons by input type**

| input | MergeSort (n=1M) | QuickSort (n=1M) | Det. Select (n=1M) | Closest Pair (n=100k) |
|---|---|---|---|---|
| random | 20,224,235 | 37,465,795 | 10,103,703 | 142,727 |
| sorted | 8,952,320 | 39,203,044 | 7,562,549 | 143,663 |
| reverse | 15,117,824 | 40,385,984 | 9,812,857 | 143,526 |
| duplicates | 19,206,009 | 5,200,170 | 3,824,187 | 104,859 |


**1. Do the results match the theoretical complexity?**
Yes. `comparisons / (n log₂ n)` is nearly constant for MergeSort (≈ 1.0) and QuickSort (≈ 1.7–1.9), and `comparisons / n` is ≈ 9–10 for Select, for all n from 1,000 to 1,000,000 (see the third plot). For n = 100,000 → 1,000,000 the time grew ×12.0 (MergeSort), ×11.1 (QuickSort) and ×10.5 (Select) versus ×12.0 (n log n) and ×10 (linear) predicted. Closest Pair, n = 10,000 → 100,000: ×12.1 (sorted) and ×11.5 (reverse); the random-input value (×6.4) is distorted by small-n noise (likely JIT/GC). Recursion depth grows logarithmically (about +2 to +4 per ×10 in n; log₂ 10 ≈ 3.3).

**2. How does input structure affect performance?**
Constants change, growth rates do not. At n = 1M: MergeSort is 3.6× faster on sorted input than on random (23.6 vs 84.7 ms) because the merge needs fewer comparisons (8.95M vs 20.2M). QuickSort with a random pivot is not degraded by sorted/reverse input (≈ 48–50 ms, depth 12) and is fastest on duplicates (12.2 ms, depth 1) thanks to the three-way partition; with a two-way partition all equal keys go to one side and it degrades to quadratic. Select: 28.1 ms random vs 8–12 ms on the other inputs. Closest Pair (n = 100k): 51–80 ms on all inputs.

**3. Why does smaller-first recursion help QuickSort?**
The part we recurse into has at most (n−1)/2 elements, so the depth is ≤ log₂ n for any pivot choices; the larger part is processed by the loop in the same stack frame. Measured maximum depth at n = 1M is 12 on random, sorted and reverse input (log₂ n ≈ 19.9). The JVM stack is finite and HotSpot does not guarantee tail-call optimisation, so O(n) depth could cause `StackOverflowError`.

**4. Why does Median-of-Medians guarantee O(n)?**
With groups of 5 the pivot has ≥ 3n/10 elements on each side, so the recursive call has ≤ 7n/10 elements, plus one call on n/5 elements for the median of medians. Since 1/5 + 7/10 = 9/10 < 1, the work per level shrinks geometrically and the total is Θ(n). Measured: ≈ 9–10 comparisons per element for every n (9.08, 9.72, 9.94, 10.10).

**5. Why is divide-and-conquer Closest Pair faster than O(n²) for large inputs?**
Only strip points are examined in the combine step and each is compared with a constant number of neighbours in y-order, so each of the log₂ n levels costs Θ(n). For n = 100,000 brute force needs n(n−1)/2 = 4,999,950,000 distance evaluations; the algorithm needed 142,727 (random input) and ran in 79.6 ms.

**6. What practical factors affect performance?**
- *JIT warm-up:* early runs are interpreted, so small inputs are noisy (Closest Pair, n = 1,000: 0.911 ms random vs 0.423 ms sorted); hence warm-up and averaging.
- *Branch prediction / cache:* MergeSort on sorted data is 3.6× faster although it makes only 2.3× fewer comparisons.
- *Allocation and GC:* Closest Pair sorts `Point` objects and allocates arrays and a `HashSet` per recursion node: 79.6 ms for n = 100,000 vs 7.0 ms for MergeSort on `int[]`.
- *Stack size:* limits recursion depth (see question 3).
- Background processes and CPU frequency scaling add noise.

---

## E. Reflection

Most of the troubles came from the fact, that I am weak with working with Java, so I had to often ask AI to help correct, mistakes in code, or just show the possible correct segment of the code, so I could have even a possibility to complete this assignment. But on the other hand, I at least got understanding of these code principles. They are somewhat similar to the ones, that we were studying in ADS course in 3rd trimester, so that also helped a little.
