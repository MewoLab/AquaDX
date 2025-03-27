
# maimai2 dev notes

## Item types

| ItemKind | Name                    |
|----------|-------------------------|
| 1        | Nameplate               |
| 2        | Title                   |
| 3        | Icon                    |
| 5        | Music Unlock            |
| 6        | Music Master Unlock     |
| 7        | Music Re:Master Unlock  |
| 8        | Music Strong Unlock (?) |
| 9        | Character               |
| 10       | Partner                 |
| 11       | Frame                   |
| 12       | Tickets                 |
| 13       | Mile                    |
| 14       | Intimate Item           |
| 15       | Kaleidx Scope Key       |

## Multiplayer

### Party Host/Client/Member

Manager.Party.Party/**Host.cs** : Host : 
* TCP **Listen** 50100 (Accept into Member)
* UDP Broadcast 50100
  * Send: StartRecruit, FinishRecruit

PartyLink/**Party.cs** : Party.Host : Exact same as Host.cs

Manager.Party.Party/**Member.cs** : Member :
* TCP Connect 50100
  * Send: JoinResult, Kick, StartPlay, StartClientState, PartyMember{Info/State}, PartyPlayInfo, RequestMeasure
  * Recv: RequestJoin, ClientState, ClientPlayInfo, UpdateMechaInfo, ResponseMeasure, FinishNews

PartyLink/**Party.cs** : Party.Member : Exact same as Member.cs

Manager.Party.Party/**Client.cs** : Client :
* UDP **Listen** 50100
  * Recv: StartRecruit, FinishRecruit
* TCP Connect 50100
  * Recv: JoinResult, Kick, StartPlay, StartClientState，PartyMember{Info/State}, PartyPlayInfo, RequestMeasure
  * Send: RequestJoin, ClientState, ClientPlayInfo, UpdateMechaInfo, ResponseMeasure, FinishNews

PartyLink/**Party.cs** : Party.Client : Exact same as Client.cs

**Enums**
* **ClientStateID**: {Setup, Wait, Connect, Request, Joined, FinishSetting, ToReady, BeginPlay, AllBeginPlay, Ready, Sync, Play, FinishPlay, News, NewsEnd, Result, Disconnected, Finish, Error}
* **JoinResult**: {Success, Full, NoRecruit, Disconnect, AlreadyJoined, DifferentGroup, DifferentMusic, DifferentEventMode}

**Models**
* **MechaInfo**: IsJoin (bool), IP Address, MusicID, Entries[2], UserIDs[2], Rating[2], ...
* **RecruitInfo**: MechaInfo, MusicID, GroupID, EventModeID, JoinNumber, PartyStance, Start time, Recv time
* **MemberPlayInfo**: IP Address, Rankings[2], Achieves[2], Combos[2], Miss[2], ...
* **ChainHistory**: PacketNo (int), Chain (int)

**Commands**
* **StartRecruit/FinishRecruit**: RecruitInfo
* **JoinResult**: JoinResult (enum)
* **RequestJoin**: MechaInfo, GroupID, EventModeID
* **UpdateMechaInfo**: MechaInfo
* **Kick**: RecruitInfo, KickBy {Cancel, Start, Disconnect}
* **RequestMeasure/ResponseMeasure**: {} - Sync delay
* **StartPlay**: MaxMeasure (long), MyMeasure (long) - Sync delay
* **StartClientState**: ClientStateID (enum)
* **ClientState**: ClientStateID (enum)
* **PartyMemberInfo**: MechaInfo[2]
* **PartyMemberState**: ClientStateID[2]
* **PartyPlayInfo**: MemberPlayInfo[2], ChainHistory[10], Chain (int), ChainMiss (int), MaxChain (int), IsFullChain (bool), CalcStatus (int)
* **ClientPlayInfo**: IP Address, Count, IsValids[2], Achieves[2], Combos[2], Miss[2], ...
* **FinishNews**: IP Address, IsValids[2], GaugeClears[2], GaugeStockNums[2]

### Setting Host/Client/Member

> This might be for synchronizing event settings across different cabs, 
> I'm not sure if this is relevant for multiplayer.

PartyLink/**Setting.cs** : Setting.**Host** :
* TCP **Listen** 50101 (Accept into Setting.Member)
* UDP Broadcast 50101
    * Send: SettingHostAddress
* UDP **Listen** 50101
    * Recv: SettingHostAddress (Check duplicate host)

PartyLink/**Setting.cs** : Setting.**Client** :
* TCP Connect 50101
    * Send: SettingRequest
    * Recv: SettingResponse, HeartBeat{}
* UDP **Listen** 50101
    * Recv: SettingHostAddress

PartyLink/**Setting.cs** : Setting.**Member** :
* TCP Connect 50101
    * Recv: SettingRequest, HeartBeat{}
    * Send: SettingResponse, HeartBeat{}

**Models**
* **SettingHostAddress**: IP Address (u32), Group (int)
* **SettingRequest**: Group (int)
* **SettingResponse**: Group (int), Data (isEventMode, eventModeMusicCount, memberNumber)

### Advertise

> For finding IP addresses of other cabs and checking their latency.

PartyLink/**Advertise.cs** : Advertise.Manager :
* UDP **Listen** 50102
  * Recv: AdvertiseRequest, AdvertiseResponse, AdvertiseGo
* UDP Broadcast 50102
  * Send: AdvertiseRequest, AdvertiseResponse, AdvertiseGo

**Models**
* **AdvertiseRequest**: IP Address (u32), Group (int), Kind (int)
* **AdvertiseResponse**: IP Address (u32), Group (int), Kind (int)
* **AdvertiseGo**: IP Address (u32), Group (int), Kind (int), MaxUsec (long), MyUsec (long)

!! sendTo is not necessarily broadcast !!

### DeliveryChecker

PartyLink/**DeliveryChecker** : DeliveryChecker.Manager :
* UDP **Listen** 50103
  * Recv: AdvocateDelivery
* UDP Broadcast 50103
  * Send: AdvocateDelivery

**Models**
* **AdvocateDelivery**: IP Address (u32)
