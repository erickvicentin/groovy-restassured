import groovy.json.JsonSlurper

def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parentFile.parentFile
def inputFile = new File(scriptDir, "resources/urls.json")

def jsonSlurper = new JsonSlurper()
def jsonObject = jsonSlurper.parseText(inputFile.text)

// Accede a los valores del objeto JSON
println(jsonObject.baseUrl+jsonObject.books)
