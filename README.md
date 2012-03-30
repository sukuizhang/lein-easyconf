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
it load all config var in your current project, create
a namepaces named config.autocreated, and then put it into test path.
if you want change the path config namespace put into, you can add a
parameter to special this path, as follow: 
    lein gen-conf you-path

### change defconf-ns-prefixs.
gen-conf plugin load config var in all namespace that's name start
with one of defconf-ns-prefixs. in default, defconf-ns-prefixs is
[your-project-name], if you add defconf-ns-prefix attribute to your
project like below:
```clojure
:defconf-ns-prefix "your new prefix"
```
then your defconf-ns-prefixs change to [your-new-prefix]
and even you add defconf-ns-prefixs attribute to your project like
below:
```clojure
:defconf-ns-prefixs ["your new prefix1" "your new prefix2" ...]
```
then your defconf-ns-prefixs change to ["your new prefix1" "your new prefix2" ...]
