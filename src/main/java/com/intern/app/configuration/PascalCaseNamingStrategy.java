package com.intern.app.configuration;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PascalCaseNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        if (name == null) {
            return null;
        }
        return Identifier.toIdentifier(convertToPascalCase(name.getText()));
    }

    // Override table name conversion if needed
    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        if (name == null) {
            return null;
        }
        return Identifier.toIdentifier(convertToPascalCase(name.getText()));
    }

    private String convertToPascalCase(String text) {
        // Ensure first letter is uppercase, rest remains unchanged
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
