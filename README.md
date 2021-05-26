## Frontend Vue.js com um back-end Spring Boot

# 1. Visão Geral
Neste tutorial, veremos um aplicativo de exemplo que renderiza uma única página com um front-end Vue.js, enquanto usa Spring Boot como back-end.

Também utilizaremos o Thymeleaf para passar informações para o modelo.

# 2. Configuração do Spring Boot
O aplicativo pom.xml usa a dependência spring-boot-starter-thymeleaf para renderização de template junto com o usual spring-boot-starter-web:

```
<dependency> 
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId> 
    <version>2.4.0</version>
</dependency> 
<dependency> 
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId> 
    <version>2.4.0</version>
</dependency>
```

Por padrão, o Thymeleaf procura por modelos de visualização em templates/, vamos adicionar um index.html vazio a src/main/resources/templates/index.html. Atualizaremos seu conteúdo na próxima seção.

Finalmente, nosso controlador Spring Boot estará em src/main/java:

```
@Controller
public class MainController {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("eventName", "FIFA 2018");
        return "index";
    }
}
```

Este controlador renderiza um único template com dados passados para a visão por meio do objeto Spring Web Model usando model.addAttribute.

Vamos executar o aplicativo usando:

```
mvn spring-boot:run
```

Navegue até http://localhost:8080 para ver a página de índice. Estará vazio neste momento, é claro.

Nosso objetivo é fazer com que a página imprima algo assim:

```
Name of Event: FIFA 2018

Lionel Messi
Argentina's superstar

Christiano Ronaldo
Portugal top-ranked player
```

# 3. Renderizando dados usando um componente Vue.Js
### 3.1. Configuração básica do modelo
No modelo, vamos carregar o Vue.js e o Bootstrap (opcional) para renderizar a interface do usuário:

```
// in head tag

<!-- Include Bootstrap -->

//  other markup

// at end of body tag
<script 
  src="https://cdn.jsdelivr.net/npm/vue@2.5.16/dist/vue.js">
</script>
<script 
  src="https://cdnjs.cloudflare.com/ajax/libs/babel-standalone/6.21.1/babel.min.js">
</script>
```

Aqui carregamos o Vue.js de um CDN, mas você também pode hospedá-lo, se preferir.

Carregamos o Babel no navegador para que possamos escrever algum código compatível com ES6 na página sem ter que executar etapas de transpilação.

Em um aplicativo do mundo real, você provavelmente usará um processo de construção usando uma ferramenta como o Webpack e o Babel transpiler, em vez de usar o Babel no navegador.

Agora vamos salvar a página e reiniciar usando o comando mvn spring-boot: run. Nós atualizamos o navegador para ver nossas atualizações; nada interessante ainda.

A seguir, vamos configurar um elemento div vazio ao qual anexaremos nossa interface de usuário:

```
<div id="contents"></div>
```

Em seguida, configuramos um aplicativo Vue na página:

```
<script type="text/babel">
    var app = new Vue({
        el: '#contents'
    });
</script>
```

O que acabou de acontecer? Este código cria um aplicativo Vue na página. Nós o anexamos ao elemento com o seletor CSS #contents.

Isso se refere ao elemento div vazio na página. O aplicativo agora está configurado para usar o Vue.js!

### 3.2. Exibindo dados no modelo
A seguir, vamos criar um cabeçalho que mostra o atributo 'eventName' que passamos do controlador Spring e renderizá-lo usando os recursos do Thymeleaf:

```
<div class="lead">
    <strong>Name of Event:</strong>
    <span th:text="${eventName}"></span>
</div>
```

Agora vamos anexar um atributo 'data' ao aplicativo Vue para armazenar nossa matriz de dados do jogador, que é uma matriz JSON simples.

Nosso aplicativo Vue agora se parece com isto:

```
<script type="text/babel">
    var app = new Vue({
        el: '#contents',
        data: {
            players: [
                { id: "1", 
                  name: "Lionel Messi", 
                  description: "Argentina's superstar" },
                { id: "2", 
                  name: "Christiano Ronaldo", 
                  description: "World #1-ranked player from Portugal" }
            ]
        }
    });
</script>
```

Agora Vue.js conhece um atributo de dados chamado jogadores.

### 3.3. Renderizando dados com um componente Vue.js
A seguir, vamos criar um componente Vue.js chamado player-card que renderiza apenas um jogador. Lembre-se de registrar este componente antes de criar o aplicativo Vue.

Caso contrário, o Vue não o encontrará:

```
Vue.component('player-card', {
    props: ['player'],
    template: `<div class="card">
        <div class="card-body">
            <h6 class="card-title">
                {{ player.name }}
            </h6>
            <p class="card-text">
                <div>
                    {{ player.description }}
                </div>
            </p>
        </div>
        </div>`
});
```

Por fim, vamos percorrer o conjunto de jogadores no objeto de aplicativo e renderizar um componente de cartão de jogador para cada jogador:

```
<ul>
    <li style="list-style-type:none" v-for="player in players">
        <player-card
          v-bind:player="player" 
          v-bind:key="player.id">
        </player-card>
    </li>
</ul>
```

A lógica aqui é a diretiva Vue chamada v-for, que fará um loop sobre cada jogador no atributo de dados dos jogadores e renderizará um cartão de jogador para cada entrada de jogador dentro de um elemento ```<li>```.

v-bind: player significa que o componente player-card receberá uma propriedade chamada player, cujo valor será a variável de loop do player com a qual está sendo trabalhada. v-bind: key é necessária para tornar cada ```<li>``` elemento único.

Geralmente, player.id é uma boa escolha, pois já é único.

Agora, se você recarregar esta página, observe a marcação HTML gerada em devtools e ela será semelhante a esta:

```
<ul>
    <li style="list-style-type: none;">
        <div class="card">
            // contents
        </div>
    </li>
    <li style="list-style-type: none;">
        <div class="card">
            // contents
        </div>
    </li>
</ul>
```

Uma nota de melhoria do fluxo de trabalho: rapidamente se tornará complicado reiniciar o aplicativo e atualizar o navegador cada vez que você fizer uma alteração no código.

Portanto, para tornar a vida mais fácil, consulte este artigo sobre como usar o Spring Boot devtools e reinicialização automática.

# 4. Conclusão
Neste artigo rápido, vimos como configurar um aplicativo da web usando Spring Boot para back-end e Vue.js para front-end. Essa receita pode formar a base para aplicativos mais poderosos e escaláveis e é apenas um ponto de partida para a maioria desses aplicativos.