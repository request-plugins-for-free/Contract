> *You can join our [discord](https://discord.gg/yGkS3Dh) to request something like this!*

> **Note from the Developer**  
We're looking for a volunteer Java Developer's to join the RPF Team, join the discord above for more information!

---
**Contract** were designed and made for an RP server, It lets your fellow players send contracts to each other with a provided timeframe, balance and description, earn in-game balance money with [Vault](https://www.spigotmc.org/resources/vault.34315/) integration, rate them based on their work, and the ability to integrate average stars into your chat plugins with [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) integration support. It's also natively built on 1.17.1 but was tested on 1.8.8.

## Contract's Commands
> **WARNING** Contractor's (whose receive/accepts contracts) cannot receive a contract unless they have the contract.receive permission node.


| Command | Alias | Syntax | Description | Permission Node |
| ------- | ----- | ------ | ----------- | --------------- |
| `/contract create` | `/create` | `<contractor> <length> <amount> <description>` | Send a contract to the contractor | `contract.send` |
| `/contract accept` | `/accept` | `<employer>` | Accept a contract from an employer | N/A |
| `/contract deny` | `/deny` | `<employer>` | Deny a contract from an employer | N/A |
| `/contract current` | `/current` | N/A | Shows your current contract | N/A |
| `/contract rate` | `/rate` | `<contract-id> <out-of-five> <review>` | Rate the contract's work once it's done | N/A |
| `/contract history` | `/history` | `[contract-id]` | Shows you all of your contract | N/A |

## **License**  
This project is licensed on GNU General Public License v3.0.
