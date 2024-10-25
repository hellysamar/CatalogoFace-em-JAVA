# Projeto Catalogo Face
Passivo de instalação em Desktops (Windows, Linux ou MAC), CatalogoFace é um programa de repositório de informações de pessoas com foto associada.

![catalogo](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/catalogo1.png)
## Autor
Hellysamar - Estudo
## Instruções para instalação e uso do programa
### Pré requisitos
1) Ter o Java **versão 17** ou superior instalado. Testado com a versão openJDK 21 LTS que pode ser obtida no link indicado. Na instalação selecione todos os recursos conforme indicado na imagem.

[download openJDK)](https://adoptium.net/)

![openJDK](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/openjdk.png)

2) Ter um banco de dados local baseado no **MySQL 8** ou MariaDB compatível, no exemplo usei o XAMPP que pode ser obtido no link indicado.

[download xampp](https://www.apachefriends.org/)

### Instalação do banco
1) Iniciar os serviços Apache e MySQL no XAMPP, conforme indicado na imagem.

![xampp start](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/xampp1.png)

2) No navegador de internet digite: **localhost/dashboard** e selecione no menu: **phpMyAdmin** conforme indicado na imagem.

![phpmyadmin](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/xampp2.png)

3) Crie um novo banco de dados de nome **dbCatalogoFace** (sem usar acentuação) conforme em negrito.

![dbCatalogoFace](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/xampp3.png)

4) Na aba SQL, copie e cole o código abaixo e execute. (Passos 1,2 e 3 indicados na imagem)

~~~sql
CREATE TABLE tblUsuarios (registro INT PRIMARY KEY AUTO_INCREMENT, nome varchar(30) NOT NULL, foto LONGBLOB NOT NULL, endereco VARCHAR(50) NOT NULL);
~~~

![usuarios](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/xampp4.png)

### Instalação do programa
1) Em Releases faça o download do arquivo **Catalogo.jar**
2) Execute e verifique no rodapé o ícone que representa o banco de dados conectado. Se estiver com erro (conforme indicado na figura) verifique o XAMPP e revise novamente os passos 1 a 4 da instalação do banco.

![programa](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/Catalogo2.png)

3) Se tudo estiver OK ( 1 ) você pode iniciar gerando uma listagem dos usuarios cadastrados ( 2 ) ou pesquisar um usuario pelo RA ou Nome ( 3 ), neste caso o programa libera os botões e recursos de acordo com o resultado da pesquisa, por exemplo se não tiver um usuario cadastrado ele libera os botões para carregar foto e adicionar e se existir um usuario cadastrado ele traz todas as informações e libera os botões para editar e excluir.

![programa](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/Catalogo3.png) 

### Modelo Relatório com dados e foto
![pdf](https://github.com/hellysamar/CatalogoFace-em-JAVA/blob/main/Assets/Catalogo4.png) 

### Bibliotecas usadas neste projeto
[mysql](https://dev.mysql.com/downloads/connector/j/)

[itextpdf](https://github.com/itext/itextpdf)

### Fonte de estudo
[Curso do Professor José de Assis](https://github.com/professorjosedeassis/)
