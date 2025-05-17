# Context-Free Grammar and Pushdown Automata Simulator

![Java](https://img.shields.io/badge/Java-17-blue)
![Automata](https://img.shields.io/badge/Formal_Languages-CFG/PDA-green)

A Java implementation for context-free language processing using CFG derivation and PDA simulation.

## Key Features

### Context-Free Grammar Engine
- **Language Recognition** for:
  - Equal counts of 'a's and 'b's
  - Twice as many 'b's as 'a's
  - Non-palindromic strings
  - Pattern `aⁿ⁺³bⁿ` where n > 0
  - Pattern `aⁿbᵐ` where n > m > 0

### Pushdown Automata Simulator
- **PDA Implementations** for:
  - Balanced `aⁿbⁿcⁿ` patterns
  - `a³ⁿb²ⁿ` patterns (n ≥ 1)
  - Balanced bracket expressions
  - `aⁿbⁿ⁺ᵐcᵐ` patterns
  - Strings with trailing `c` count matching `b` count

## Technical Implementation

- **CFG Core**:
  - Terminal/non-terminal symbol sets
  - Production rule system
  - Leftmost derivation strategy

- **PDA Core**:
  - Stack operations (push/pop)
  - State transition handling
  - ϵ-transition support
  - Acceptance by final state




## Contributors

We would like to thank the following contributors to this project:

- [**Shahd Osama**](https://github.com/shahdosama10).
- [**Ahmed Saad**](https://github.com/ahmedsaad123456).
- [**Ahmed Adel**](https://github.com/Dola112).
