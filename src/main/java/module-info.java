module br.pucpr.projeto {
    requires javafx.controls;
    requires javafx.fxml;

    // Export main package
    exports br.pucpr.projeto;

    // Export subpackages if they contain classes that need to be accessible
    exports br.pucpr.projeto.Metas;
    exports br.pucpr.projeto.Principal;
    exports br.pucpr.projeto.Carteira;

    // Open packages for JavaFX FXML reflection
    opens br.pucpr.projeto to javafx.fxml;
    opens br.pucpr.projeto.Metas to javafx.fxml;
    opens br.pucpr.projeto.Principal to javafx.fxml;
    opens br.pucpr.projeto.Carteira to javafx.fxml;

    // Removi as referências ao pacote dominio que não existe mais
}