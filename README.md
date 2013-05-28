lodil
=====

LOcal DIctionary Lib, Just opposite to REmote DIctionary Server, aka Redis

Redis 全称是 Remote Dictionary Server，对应的，我们可以考虑做一个 Local
Dictionary Lib （简称 lodil，不过这个名字不是很好听，同时征集命名 :-），
类似 ehcache + aof/wal + 本地 SSD 存储，即可靠的嵌入式 in-jvm 的缓存+存
储一体化解决方案，提供 kv，key-list，key-hash 等几种常见的数据结构读写操
作，并且数据是落地的。

基本上，lodil 大概是这样的一个东东：

1. 类似 Jedis 的调用界面，多种常用数据结构支持
2. 数据缓存在 jvm 内存中，本地 or rpc远程，on heap or off heap，gc 友好
3. 数据实时持久化，wal + snapshot
4. LRU 和其它 cache 淘汰算法的支持

类似的应用场景为：

1. poly 及后续的升级版本
2. user 服务：将所有用户都加载到 lodil 后，后端只需要极少的 mysql 作为数据可靠性保证即可
3. 用户关系服务：日活跃用户的关注列表加载到内存，其它用户关注列表在 ssd 文件中
4. Queue service，去掉对 bdb 的依赖，java 直接写文件

等等。

本质上来说，等于用 Java 实现一个简单的存储引擎，将业务与数据更紧密的合并
起来进行处理（我们当前的做法都是业务与数据分离的），并将 Java 业务逻辑处
理的数据范围从目前的仅限于内存拓展到磁盘范围（初步考虑只支持 ssd 盘）
