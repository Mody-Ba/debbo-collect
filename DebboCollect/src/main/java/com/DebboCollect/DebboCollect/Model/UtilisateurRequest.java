package com.DebboCollect.DebboCollect.Model;

import com.DebboCollect.DebboCollect.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;
    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

}
/* role de cette classe quand swagger envoie {
  "nom": "Mody",
  "email": "mody@gmail.com",
  "motDePasse": "1234",
  "role": "ADMIN"
}Spring reçoit ça dans  UtilisateurRequest*/
