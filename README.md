# Jedis Demo

此 Repo 是圍繞在 [Jedis guide](https://redis.io/docs/latest/develop/connect/clients/java/jedis/) 展開的。

## JSON

參考官方[測試範例source code](https://github.com/redis/jedis/blob/master/src/test/java/redis/clients/jedis/modules/json/RedisJsonV2Test.java)

`Path2.ROOT_PATH` 的本質是 `$` 字號，詳見[source code](https://github.com/redis/jedis/blob/master/src/main/java/redis/clients/jedis/json/Path2.java#L8)

`Path2.of` 的用法:
* `Path2.of("..")` 根路徑底下全部
* `Path2.of("..a")` 根路徑底下全部的a
* `Path2.of("$[*]")` 根路徑下所有的陣列元素
* `Path2.of(".a.b")` 根路徑底下a.b