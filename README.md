# MultipleVersionKafka

在同一个工程下中支持不同的 kafka 版本。

说下为什么要支持这么奇怪的特性：

因为在我们的 **生产环境** 有一个需求，我们有一个 ETL 任务，数据源是 `0.9.0.1` 的 server，而接收数据的是 `2.2.0` 的 server。并且，接收方用到了 `2.2.0` 的新特性。这引发了一个神奇的问题：

- 如果我使用 `0.9.0.1` 的 client，那么我没办法使用接收方要求的新特性。
- 如果我使用 `2.2.0` 的 client，又会因为 client 版本高过服务端导致无法正常消费 `0.9.0.1` 的 server。
- 对于同一个包， **Maven will pick one version using the "`nearest in the dependency tree`" strategy**。

## 测试

1. 代码根目录下执行 `mvn clean package`；
2. 启动本地 kafka 及其依赖进程；
