@Grab('groovy-all')
@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')

import groovy.json.*
import groovyx.net.http.*
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*


@ConfigurationProperties(prefix = 'es')
class EsExportConfigProperties {
    String host = '192.168.33.11'
    int port = 9200
}

@Component
class EsExport implements CommandLineRunner {
    @Autowired
    EsExportConfigProperties esConfig

    @Override
    void run(String... args) {
        def json = new JsonBuilder()
        def sortConditions = ["age"]
        if (args.length > 0) {
            if (args[0] == "solution") {
                sortConditions += "account_number" // without this, the result will bounce
            }
        }
        json {
            from 0
            size 1
            explain true
            query {
              match_all {}
            }
            sort sortConditions
        }
        println "Query: ${json.toString()}"
        def http = new RESTClient("http://${esConfig.host}:${esConfig.port}/")
        def shards = http.get(path: '_cluster/state', contentType: JSON).data.routing_table.indices.bank.shards
        def hits = [:]
        def idx = 1
        while (true) {
            def result = http.post(path: 'bank/account/_search', contentType: JSON, body: json.toString()).data
            if (result.hits.hits.size() == 0) {
                break
            }
            result.hits.hits.each { hit ->
                if (!hits.containsKey(hit._id)) {
                    hits[hit._id] = [:]
                }
                hits[hit._id][idx] = "shard${hit._shard}(${shards[hit._shard.toString()].find { it.node == hit._node }.primary ? 'primary' : 'replica'})"
                //println JsonOutput.toJson([_id: hit._id])
            }
            json.content.from += json.content.size
            idx++
        }
        def duplicates = hits.findAll { k, v -> v.size() > 1 }
        if (duplicates.size() == 0) {
            println "No duplicate result"
        } else {
            println "Duplicate results"
            duplicates.each { k, v ->
                println "_id: ${k} -> ${v.size()} times: ${v}"
            }
        }
    }
}
