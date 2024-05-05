# Encoding of actions

## Per action: 3, 5 or 10 bits
The following actions use the following amounts of bits.
### Placing a tile: 10 bits
- 2 bits for the rotation
- 8 bits for the tile id

### Occupying a tile: 5 bits
- 1 bit for the occupant type
- 4 bits for the zone id

### Retaking a pawn: 5 bits
- 5 bits for which pawn out of the max 25 possible pawns on the board

## Encoding: Base32
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26 | 27 | 28 | 29 | 30 | 31 |
|---|---|---|---|---|---|---|---|---|---|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|----|
| A | B | C | D | E | F | G | H | I | J | K  | L  | M  | N  | O  | P  | Q  | R  | S  | T  | U  | V  | W  | X  | Y  | Z  | 2  | 3  | 4  | 5  | 6  | 7  |

Example: CH<sub>32</sub> = 2 * 32 + 7 = 71<sub>10</sub>

Actions are encoded using either 1 or two base32 characters:

| Action           | char 1 | char 2 |
|------------------|--------|--------|
| Placing a tile   | ppppp  | ppprr  |
| Occupying a tile |        | kzzzz  |
| Retaking a pawn  |        | ooooo  |

The `p` bits are used for the index of the tile to be placed in the fringe, between 0 (inc) 
and 190 (exc). For the indexing, these tile positions are sorted lexicographically sorted by their
(x, y) coordinates in ascending order.  

The `r` bits represent the rotation as the index of the Rotation enum. Thus, `00 = NONE`, 
`01 = RIGHT`, `10 = HALF_TURN`, `11 = LEFT`.  

The `k` bit represents the occupant type, where `0 = PAWN` and `1 = HUT`.  
The `z` bits are used for the id of the zone to be occupied, between 0 (inc) and 10 (exc). The case
where no occupant is placed is represented by `11111`.

The `o` bits represent the index of the pawn to be retaken out of __all__ available pawns on the
board, between 0 (inc) and 25 (exc). To be indexed, the pawns are sorted in ascending order by the
id of the zone they occupy. The case where no occupant is retaken is represented by `11111`.

### Example
In the beginning of a game, the fringe contains the following positions, sorted and indexed by their
coordinates:

| Index | Position |
|-------|----------|
| 0     | (-1, 0)  |
| 1     | (0, -1)  |
| 2     | (0, 1)   |
| 3     | (1, 0)   |

Say, the first player places the next tile to the north of the starting tile, thus in position 
(0, -1) and rotates it by a half turn. The index of the position is 1 and the rotation has the
index 2. Thus the action would be encoded as:
```
00000 00110     =       AG  (base32)
```

Now, the first player occupies the tile with a hut (index 1) in zone 3, this action would be 
encoded as:
```
10011           =       T   (base32)
```