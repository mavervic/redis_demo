# Jedis Demo

此 Repo 是圍繞在 [Jedis guide](https://redis.io/docs/latest/develop/connect/clients/java/jedis/) 展開的。

## JedisPooled

如果對 JedisPooled 有興趣，可以從測試文件開始著手，透過assert可以比較簡單的知道每個api的用途以及預期的結果

參考官方[JedisPooledTest.java](https://github.com/redis/jedis/blob/master/src/test/java/redis/clients/jedis/JedisPooledTest.java)

## JSON

參考官方[測試範例source code](https://github.com/redis/jedis/blob/master/src/test/java/redis/clients/jedis/modules/json/RedisJsonV2Test.java)

`Path2.ROOT_PATH` 的本質是 `$` 字號，詳見[source code](https://github.com/redis/jedis/blob/master/src/main/java/redis/clients/jedis/json/Path2.java#L8)

`Path2.of` 的用法:
* `Path2.of("..")` 根路徑底下全部
* `Path2.of("..a")` 根路徑底下全部的a
* `Path2.of("$[*]")` 根路徑下所有的陣列元素
* `Path2.of(".a.b")` 根路徑底下a.b

## 目錄結構說明

本專案之 `src` 目錄結構及各資料夾用途說明如下：

```
└─src
    ├─example
    │  ├─base64
    │  │   # Base64 編碼/解碼範例，展示如何將圖片等二進位資料轉為字串與還原。
    │  ├─deprecated
    │  │   # 舊範例，包含 Java 物件與 JSON 互轉、字串化等基本操作。
    │  ├─jedis
    │  │  ├─basic
    │  │  │   # Jedis 連線與最基本的 Redis 操作範例。
    │  │  ├─transaction
    │  │  │   # Redis 交易（Transaction）相關範例。
    │  │  └─types
    │  │      ├─hash
    │  │      │   # Redis Hash 資料型態操作範例。
    │  │      ├─json
    │  │      │   # Redis JSON（ReJSON）進階操作範例，包含插入、查詢、更新、合併等。
    │  │      ├─list
    │  │      │   # Redis List 資料型態操作範例。
    │  │      ├─set
    │  │      │   # Redis Set 資料型態操作範例。
    │  │      ├─string
    │  │      │   # Redis String 資料型態操作範例，含庫存、驗證碼等應用。
    │  │      └─zset
    │  │          # Redis Sorted Set（ZSet）資料型態操作範例。
    │  └─json
    │      # JSON 相關的額外範例（如有）。
    └─utils
        # 共用工具類別，例如 Redis 連線工具、Mock 資料產生器等。
```
