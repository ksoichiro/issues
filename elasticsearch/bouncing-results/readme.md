# Reproducing Elasticsearch bouncing results problem

Quoted from [Elasticsearch: The Definitive Guide](https://www.elastic.co/guide/en/elasticsearch/guide/current/_search_options.html):

> ### Bouncing Results
> 
> Imagine that you are sorting your results by a timestamp field, and two documents have the same timestamp. Because search requests are round-robined between all available shard copies, these two documents may be returned in one order when the request is served by the primary, and in another order when served by the replica.
>   
> This is known as the bouncing results problem: every time the user refreshes the page, the results appear in a different order. The problem can be avoided by always using the same shards for the same user, which can be done by setting the preference parameter to an arbitrary string like the user’s session ID.


## Steps to reproduce

### Prepare Elasticsearch cluster

Clone [ksoichiro/vagrant-templates](/centos71-elasticsearch](https://github.com/ksoichiro/vagrant-templates/) repository and start Elasticsearch servers:

```
$ cd vagrant-templates/centos71-elasticsearch
$ vagrant up es1 es2 es3
```

### Bulk insert for each nodes

```
$ spring run bulk.groovy
1000 docs
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
node: 192.168.33.11, errors: false, count: 50
```

> [sdkman](http://sdkman.io/) and Spring Boot CLI 1.3.2.RELEASE is required.
> 
> ```
> curl -s "https://get.sdkman.io" | bash
> sdk install springboot 1.3.2.RELEASE
> ```

### Search

```
$ spring run search.groovy
Query: {"from":0,"size":1,"explain":true,"query":{"match_all":{}},"sort":["age"]}
Duplicate results
_id: 152 -> 2 times
_id: 373 -> 2 times
_id: 582 -> 2 times
_id: 859 -> 2 times
_id: 924 -> 2 times
_id: 525 -> 2 times
_id: 272 -> 2 times
_id: 835 -> 2 times
_id: 897 -> 2 times
_id: 102 -> 2 times
_id: 789 -> 2 times
_id: 462 -> 2 times
_id: 506 -> 2 times
_id: 107 -> 2 times
_id: 708 -> 2 times
_id: 176 -> 2 times
_id: 758 -> 2 times
_id: 866 -> 2 times
_id: 289 -> 2 times
_id: 563 -> 2 times
_id: 51 -> 2 times
_id: 638 -> 2 times
_id: 195 -> 2 times
_id: 277 -> 2 times
_id: 429 -> 2 times
_id: 1 -> 2 times
_id: 777 -> 2 times
_id: 121 -> 2 times
_id: 145 -> 2 times
_id: 467 -> 2 times
_id: 602 -> 2 times
_id: 823 -> 2 times
_id: 955 -> 2 times
_id: 32 -> 2 times
_id: 296 -> 2 times
_id: 330 -> 2 times
_id: 304 -> 2 times
_id: 417 -> 2 times
_id: 513 -> 2 times
_id: 760 -> 2 times
_id: 37 -> 2 times
_id: 99 -> 2 times
_id: 138 -> 2 times
_id: 498 -> 2 times
_id: 549 -> 2 times
_id: 878 -> 2 times
_id: 948 -> 2 times
```

If you cannot reproduce it, run `spring run bulk.groovy` again.

### One of the solution

Add another sort condition to ensure the order.

```
spring run search.groovy solution
```

Other solutions:

- add preference search option
- use scan and scroll API

### Change number of replicas

```
curl -XPUT 192.168.33.11:9200/_settings -d '
{
    "index" : {
        "number_of_replicas" : 0
    }
}'
```
