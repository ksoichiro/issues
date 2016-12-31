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
_id: 246 -> 2 times: [64:shard2(primary), 71:shard2(replica)]
_id: 75 -> 2 times: [114:shard2(primary), 119:shard2(replica)]
_id: 385 -> 2 times: [115:shard2(replica), 118:shard2(primary)]
_id: 227 -> 2 times: [116:shard2(primary), 121:shard2(replica)]
_id: 688 -> 2 times: [117:shard2(replica), 120:shard2(primary)]
_id: 436 -> 2 times: [159:shard2(replica), 160:shard2(primary)]
_id: 734 -> 2 times: [161:shard2(replica), 162:shard2(primary)]
_id: 210 -> 2 times: [200:shard2(primary), 209:shard2(replica)]
_id: 450 -> 2 times: [243:shard2(replica), 244:shard2(primary)]
_id: 640 -> 2 times: [245:shard2(replica), 246:shard2(primary)]
_id: 328 -> 2 times: [345:shard2(replica), 346:shard2(primary)]
_id: 551 -> 2 times: [347:shard2(replica), 348:shard2(primary)]
_id: 13 -> 2 times: [382:shard2(primary), 387:shard2(replica)]
_id: 811 -> 2 times: [391:shard2(replica), 392:shard2(primary)]
_id: 63 -> 2 times: [478:shard2(primary), 481:shard2(replica)]
_id: 309 -> 2 times: [479:shard2(replica), 482:shard2(primary)]
_id: 809 -> 2 times: [483:shard2(replica), 484:shard2(primary)]
_id: 253 -> 2 times: [519:shard2(replica), 522:shard2(primary)]
_id: 366 -> 2 times: [521:shard2(replica), 524:shard2(primary)]
_id: 626 -> 2 times: [523:shard2(replica), 526:shard2(primary)]
_id: 986 -> 2 times: [527:shard2(replica), 528:shard2(primary)]
_id: 316 -> 2 times: [581:shard2(replica), 586:shard2(primary)]
_id: 56 -> 2 times: [582:shard2(primary), 585:shard2(replica)]
_id: 520 -> 2 times: [583:shard2(replica), 588:shard2(primary)]
_id: 380 -> 2 times: [631:shard2(replica), 634:shard2(primary)]
_id: 70 -> 2 times: [632:shard2(primary), 637:shard2(replica)]
_id: 587 -> 2 times: [633:shard2(replica), 636:shard2(primary)]
_id: 739 -> 2 times: [635:shard2(replica), 638:shard2(primary)]
_id: 828 -> 2 times: [639:shard2(replica), 640:shard2(primary)]
_id: 169 -> 2 times: [684:shard2(primary), 693:shard2(replica)]
_id: 342 -> 2 times: [785:shard2(replica), 790:shard2(primary)]
_id: 20 -> 2 times: [786:shard2(primary), 795:shard2(replica)]
_id: 378 -> 2 times: [787:shard2(replica), 792:shard2(primary)]
_id: 222 -> 2 times: [788:shard2(primary), 799:shard2(replica)]
_id: 455 -> 2 times: [789:shard2(replica), 794:shard2(primary)]
_id: 599 -> 2 times: [791:shard2(replica), 796:shard2(primary)]
_id: 727 -> 2 times: [793:shard2(replica), 798:shard2(primary)]
_id: 44 -> 2 times: [834:shard2(primary), 837:shard2(replica)]
_id: 993 -> 2 times: [839:shard2(replica), 840:shard2(primary)]
_id: 25 -> 2 times: [920:shard2(primary), 923:shard2(replica)]
_id: 575 -> 2 times: [921:shard2(replica), 928:shard2(primary)]
_id: 82 -> 2 times: [922:shard2(primary), 925:shard2(replica)]
_id: 126 -> 2 times: [924:shard2(primary), 927:shard2(replica)]
_id: 873 -> 2 times: [929:shard2(replica), 930:shard2(primary)]
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
