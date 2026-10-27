import 'package:flutter/material.dart';

/// Identidade visual do ToolRent.
///
/// Toda cor, espaçamento e tipografia do app sai daqui. Nenhum widget define
/// cor literal — assim um ajuste de identidade acontece em um lugar só.
class AppTheme {
  AppTheme._();

  // ---------------------------------------------------------------
  // Paleta
  // Azul industrial como cor principal, âmbar como destaque — a
  // combinação remete a ferramenta e sinalização de obra.
  // ---------------------------------------------------------------

  static const Color azulPrincipal = Color(0xFF1E40AF);
  static const Color azulEscuro = Color(0xFF1E3A8A);
  static const Color azulClaro = Color(0xFFDBEAFE);

  static const Color ambar = Color(0xFFF59E0B);
  static const Color ambarEscuro = Color(0xFFB45309);

  static const Color verdeSucesso = Color(0xFF047857);
  static const Color vermelhoErro = Color(0xFFB91C1C);
  static const Color cinzaTexto = Color(0xFF0F172A);
  static const Color cinzaSuave = Color(0xFF64748B);
  static const Color fundoClaro = Color(0xFFF8FAFC);

  // ---------------------------------------------------------------
  // Espaçamento — escala de 4 em 4, para manter ritmo visual
  // ---------------------------------------------------------------

  static const double espacoXs = 4;
  static const double espacoSm = 8;
  static const double espacoMd = 16;
  static const double espacoLg = 24;
  static const double espacoXl = 40;

  static const double raioBorda = 12;

  // ---------------------------------------------------------------
  // Temas
  // ---------------------------------------------------------------

  static ThemeData get claro {
    final esquema = ColorScheme.fromSeed(
      seedColor: azulPrincipal,
      primary: azulPrincipal,
      secondary: ambar,
      error: vermelhoErro,
      brightness: Brightness.light,
    );

    return ThemeData(
      useMaterial3: true,
      colorScheme: esquema,
      scaffoldBackgroundColor: fundoClaro,
      appBarTheme: const AppBarTheme(
        backgroundColor: azulPrincipal,
        foregroundColor: Colors.white,
        elevation: 0,
        centerTitle: false,
      ),
      cardTheme: CardThemeData(
        elevation: 0,
        color: Colors.white,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(raioBorda),
          side: const BorderSide(color: Color(0xFFE2E8F0)),
        ),
      ),
      filledButtonTheme: FilledButtonThemeData(
        style: FilledButton.styleFrom(
          padding: const EdgeInsets.symmetric(
            horizontal: espacoLg,
            vertical: espacoMd,
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(espacoSm),
          ),
        ),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: Colors.white,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(espacoSm),
        ),
      ),
    );
  }

  static ThemeData get escuro {
    return ThemeData(
      useMaterial3: true,
      colorScheme: ColorScheme.fromSeed(
        seedColor: azulPrincipal,
        secondary: ambar,
        brightness: Brightness.dark,
      ),
    );
  }
}
