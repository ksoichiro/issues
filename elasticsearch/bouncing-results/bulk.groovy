@Grab('groovy-all')
@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')

import groovy.json.*
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*

@ConfigurationProperties(prefix = 'es')
class EsExportConfigProperties {
    String[] hosts = [
        '192.168.33.11',
        //'192.168.33.12',
        //'192.168.33.13',
    ]
    int port = 9200
}

@Component
class EsExport implements CommandLineRunner {
    @Autowired
    EsExportConfigProperties esConfig

    @Override
    void run(String... args) {
        def http = new RESTClient("http://${esConfig.hosts[0]}:${esConfig.port}/")
        try {
            http.delete(path: 'bank/')
        } catch (ignore) {
        }

        def json = new JsonBuilder()
        def lines = new File('accounts.json').readLines()
        println "${lines.size() / 2} docs"
        def i = 0
        def node = 0
        def size = 100
        for (; i <= lines.size() - size; i += size) {
            http = new RESTClient("http://${esConfig.hosts[node]}:${esConfig.port}/")
            def result = http.post(path: 'bank/account/_bulk', contentType: JSON, body: lines.subList(i, i + size).join('\r\n').toString()+'\r\n').data
            println "node: ${esConfig.hosts[node]}, errors: ${result.errors}, count: ${result.items.size()}"
            node = (node + 1) % esConfig.hosts.size()
        }
    }
}
