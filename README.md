# lein-easyconf

A lein plugin help to create config template for easyconf.
It collect all config var in project and write it to a config namespace.

2012-3-30 Release 0.1.0

## Usage

add to porject.clj

```clojure
:dev-dependencies [[lein-easyconf "0.1.0"]]
```

### use lein plugin gen-conf to create your config namespace.
Go to your project path, and run
    lein gen-conf
it load all config var in your current project, and create
namepaces. 
actually,it load all namespace that's name starts with your
project name. for example, if your project name is clj-rpc, and then
it will load config var define in clj-rpc.server,
clj-rpc.client,...,but it do not load config var define in
rmi-service.simple.
so, if your namespace name in project not start with project name, or
you want to load config var define in libs you depend.you must special
the namespace prefix you want gen-conf plugin load,like follow:
    lein gen-conf clj-rpc rmi-service ...

the namespace file created named config.autocreated, and put it into
test path. 

