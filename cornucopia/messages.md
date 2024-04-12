# Message generation
Overview of you messages are generated.

In the following, these abbreviations are used:

| abbrev. | meaning               |
|---------|-----------------------|
| #p      | number of points      |
| #t      | number of tiles       |
| #m      | number of mammoths    |
| #a      | number of aurochs     |
| #d      | number of deer        |
| #c      | number of champignons |
| #f      | number of fish        |
| #l      | number of lakes       |

## Sorting
When multiple elements of the same general kind appear in a message, they are sorted:
- Players: by Color  
  RED -> BLUE -> GREEN -> YELLOW -> PURPLE

- Animals: by value  
  MAMMOTH -> AUROCHS -> DEER  
  Tigers are never mentioned in messages

These elements are then separated by a comma and a space, the last two by the word "et".  
If some of these elements don't have an associated value (e.g. no deer in a meadow) they are simply not mentioned at all.
The correct singular/plural form needs to be chosen, in the following, plural is used everywhere.

## Menhir forest closed
\{player_name} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir. 

## Forest closed
### One majority occupant
\{player_name} a remporté \{#p} points en tant qu'occupant·e majoritaire d'une forêt composée de \{#t} tuiles et de \{#c} groupes de champignons. 

### Multiple majority occupants
\{player_names} ont remporté \{#p} points en tant qu'occupant·e·s majoritaires d'une forêt composée de \{#t} tuiles et de \{#c} groupes de champignons.

## River closed
### One majority occupant
\{player_name} a remporté \{#p} points en tant qu'occupant·e majoritaire d'une rivière composée de \{#t} tuiles et contenant \{#f} poissons.

### Multiple majority occupants
\{player_names} ont remporté \{#p} points en tant qu'occupant·e·s majoritaires d'une rivière composée de \{#t} tuiles et contenant \{#f} poissons.

## Hunting Trap (Fosse à pieux)
\{player_name} a remporté \{#p} points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de \{#m} mammouths, \{#a} aurochs et \{#d} cerfs.

## Logboat (pirogue)
\{player_name} a remporté \{#p} points en plaçant la pirogue dans un réseau hydrographique contenant \{#l} lacs. 

## End of game: Meadow
### One majority occupant
\{player_name} a remporté \{#p} points en tant qu'occupant·e majoritaire d'un pré contenant \{#m} mammouths, \{#a} aurochs et \{#d} cerfs.

### Multiple majority occupants
\{player_names} ont remporté \{#p} points en tant qu'occupant·e·s majoritaires d'un pré contenant \{#m} mammouths, \{#a} aurochs et \{#d} cerfs.

## End of game: River System
### One majority occupant
\{player_name} a remporté \{#p} points en tant qu'occupant·e majoritaire d'un réseau hydrographique contenant \{#f} poissons.

### Multiple majority occupants
\{player_names} ont remporté \{#p} points en tant qu'occupant·e·s majoritaires d'un réseau hydrographique contenant \{#f} poissons.

## End of game: Pit Trap (Grande fosse à pieux)
### One majority occupant
\{player_name} a remporté \{#p} points en tant qu'occupant·e majoritaire d'un pré contenant la grande fosse à pieux entourée de \{#m} mammouths, \{#a} aurochs et \{#d} cerfs.

### Multiple majority occupants
\{player_names} ont remporté 12 points en tant qu'occupant·e·s majoritaires d'un pré contenant la grande fosse à pieux entourée de \{#m} mammouths, \{#a} aurochs et \{#d} cerfs.

## End of game: Raft
### One majority occupant
\{player_name} a remporté \{#p} points en tant qu'occupant·e majoritaire d'un réseau hydrographique contenant le radeau et \{#l} lacs.

### Multiple majority occupants
\{player_names} ont remporté \{#p} points en tant qu'occupant·e·s majoritaires d'un réseau hydrographique contenant le radeau et \{#l} lacs.

## End of game: Winners
### One winner
\{player_name} a remporté la partie avec \{#p} points ! 

### Multiple winners
\{player_names} ont remporté la partie avec \{#p} points !

## Placement and Removal of occupants
These messages aren't shown on the message board, but rather instead of the tile which would normally be placed.

### Placement
Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.

### Removal
Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.
