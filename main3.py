import json
from typing import List, Tuple

def base_str_to_int(value: str, base: int) -> int:
    return int(value, base)

def lagrange_interpolation_at_zero(points: List[Tuple[int, int]]) -> int:
    total = 0
    k = len(points)
    for i in range(k):
        xi, yi = points[i]
        num = 1
        den = 1
        for j in range(k):
            if i != j:
                xj = points[j][0]
                num *= -xj
                den *= xi - xj
        total += yi * (num // den)
    return total

def process_test_case(filename: str):
    print(f"\n🔍 Processing: {filename}")
    try:
        with open(filename, "r") as f:
            data = json.load(f)
    except Exception as e:
        print(f"❌ Error loading {filename}: {e}")
        return

    k = data["keys"]["k"]
    entries = [(int(key), int(data[key]["base"]), data[key]["value"])
               for key in data if key != "keys"]

    # Sort by x values
    entries.sort(key=lambda x: x[0])

    # Decode the first k points
    points = []
    for x, base, val in entries[:k]:
        try:
            y = base_str_to_int(val, base)
        except ValueError as e:
            print(f"❌ Error decoding y at x = {x}: {e}")
            return
        points.append((x, y))

    # Compute the secret using Lagrange interpolation at x=0
    try:
        secret = lagrange_interpolation_at_zero(points)
        print(f"✅ The secret (constant term c) is: {secret}")
    except Exception as e:
        print(f"❌ Error computing Lagrange interpolation: {e}")

if __name__ == "__main__":
    test_files = ["input1.json", "input2.json"]
    for file in test_files:
        process_test_case(file)
