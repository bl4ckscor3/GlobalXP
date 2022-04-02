# GlobalXP Changelog

#### v1.9.2
- Added Swiss Italian translation (Thanks BlackShadow77!)

### v1.9
- Added configuration option "retrievalPercentage" to control how much percent of stored XP will be given back to the player

### v1.8
- Added configuration option "retrieval_amount" to control how much XP points are retrieved per interaction. See config file for details.
- Added configuration option "storing_amount" to control how much XP points are stored per interaction. See config file for details.

#### v1.7.1
- Added Italian translation (Thanks BlackShadow77!)

### v1.7
- Added ability to use redstone to turn off picking up XP orbs. The existing configuration option will override this.

#### v1.6.1
- Fixed XP Block not working properly

### v1.6
- Added configuration option to control whether to store only one level or all levels at once
- Added configuration option to control whether to retrieve only one level or all levels at once

### v1.5
- Added ability to pick up XP orbs (Thanks Bletch1971!)
- Added configuration option to turn off picking up XP orbs
- Added configuration option to modify the range of blocks around the XP Block in which to pickup XP orbs
- Fixed configuration option for comparator output not working properly. The config option has been moved to server config (found in the serverconfig folder in the world folder)

#### v1.4.15
- Tentative fix for [#22](https://github.com/bl4ckscor3/GlobalXP/issues/22)

#### v1.4.14
- Fixed block interaction in claimed FTB Chunks

#### v1.4.13
- Reenable HWYLA integration

#### v1.4.12
- Reenable and fix The One Probe integration

#### v1.4.11
- Fixed compatibility with Forge 30.0.17+

#### v1.4.10
- Added Polish translation (Thanks kacpergibas438!)

#### v1.4.9
- Translation fixes

#### v1.4.8
- Added comparator functionality. It can now read how much XP is in the block and gives off a redstone signal based on that. 30 levels is a full signal.
- Added configuration option to change how much XP is needed for the comparator to output a redstone signal of strength one
- Fixed block model flickering
- Fixed configuration file comments

#### v1.4.7
- Added French translation (Thanks Lykrast!)

#### v1.4.6
- Fixed block not being accessible when holding something in the offhand
- Fixed block break particles missing when breaking the block

#### v1.4.5
- Fixed XP not being calculated correctly leading to duping or loosing XP
- Fixed the mod not working on servers

#### v1.4.4
- Added Chinese translation (Thanks xuyu0v0!)

#### v1.4.3
- Fixed empty XP Block not dropping in survival mode

#### v1.4.2
- Fixed crash in 1.13.2 when loading the mod without TheOneProbe

#### v1.4.1
- Added Russian translation (Thanks martsinkevic!)
- Add back The One Probe support for 1.13.2
- Fixed XP Block dropping in creative even when it's empty
- Fixed a crash in 1.13.2

### v1.4
- Added TheOneProbe integration (Thanks Konlii!)
- The XP Block now stores XP instead of individual levels (Thanks CplPibald and Konlii!)
- The XP Block now retains its contents when broken

### v1.3
- Added configuration option to change the speed at which the emerald within the XP Block is spinning
- Added configuration option to change the speed at which the emerald is bobbing up and down

#### v1.2.1
- Added Spanish translation (Thanks Dorzar!)

### v1.2
- Ported to 1.12
- Added way to view the stored levels when looking at the block
- Fixed malformatted WAILA display

### v1.1
- Fixed issue where XP levels could not be transfered when something was held in the main hand or offhand

## v1.0
- Initial release