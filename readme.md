# API and Reader for HWg-STE devices

This project offers an API and a reader abstraction for the [HWg-STE](https://www.hw-group.com/device/hwg-ste) device.
It uses the XML available over the built-in web server to gather the requested data.


## Use as Maven dependency
The artifacts are published on GitHub Packages. Therefore, the following repository needs to be added to Maven:
```
<repository>
    <id>github</id>
    <name>GitHub fhnw-hwgste-reader Apache Maven Packages</name>
    <url>https://maven.pkg.github.com/fhnw-imvs/fhnw-hwgste-reader</url>
</repository>
```

_More information can be found in the documentation for
[Maven](https://maven.apache.org/guides/mini/guide-multiple-repositories.html) and
[GitHub Packages](https://help.github.com/en/github/managing-packages-with-github-packages/configuring-apache-maven-for-use-with-github-packages)._

### API
```
<dependency>
  <groupId>ch.fhnw.imvs</groupId>
  <artifactId>hwgste-api</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Reader
```
<dependency>
  <groupId>ch.fhnw.imvs</groupId>
  <artifactId>hwgste-reader</artifactId>
  <version>1.0.0</version>
</dependency>
```


## How to use this project

### API and `HwgSteFetcher`
```
// create node list
List<HwgSteNode> nodes = new ArrayList<>();
HwgSteNode node1 = new HwgSteNode("node-1", "127.0.0.1");
nodes.add(node1);
HwgSteNode node2 = new HwgSteNode("node-2", "127.0.0.2");
nodes.add(node2);

// create a fetcher
HwgSteFetcher fetcher = new HwgSteFetcher();

// get the data from the nodes
List<HwgSteReading> readings = fetcher.fetch(nodes);
```


### Reader

#### Synchronous read with `HwgSteReader`
```
HwgSteReader reader = new HwgSteReader();

reader.attachNode(new HwgSteNode("node-1", "127.0.0.1"));
reader.attachNode(new HwgSteNode("node-2", "127.0.0.2"));

List<HwgSteReading> readings = reader.read();
```

#### Periodic reading with `HwgStePeriodicReader`

Use the functional interface or a method reference:
```
HwgSteMeasurementCallback callback = readings -> doSomething(readings);

HwgStePeriodicReader reader = new HwgStePeriodicReader(callback, 2);

reader.attachNode(new HwgSteNode("node-1", "127.0.0.1"));
reader.attachNode(new HwgSteNode("node-2", "127.0.0.2"));

reader.start();
```

Implement the interface and pass an instance of the implementation:
```
HwgStePeriodicReader reader = new HwgStePeriodicReader(new Callback(), 2);

reader.attachNode(new HwgSteNode("node-1", "127.0.0.1"));
reader.attachNode(new HwgSteNode("node-2", "127.0.0.2"));

reader.start();

// ...

class Callback implements HwgSteMeasurementCallback {
    @Override
    public void handle(final List<HwgSteReading> readingList) {
        // do something
    }
}
```

## Remarks
The project has been tested with the following sensors:
* [Temp-1Wire IP67 3m](https://www.hw-group.com/sensor/temp-1wire-ip67-1m-3m-10m)
* [Humid-1Wire 3m](https://www.hw-group.com/sensor/humid-1wire-1m-3m-10m)

## License
This project is licensed under the Apache 2.0 license, see [LICENSE.md](LICENSE.md).
