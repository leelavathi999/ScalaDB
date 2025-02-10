package core

import org.mindrot.jbcrypt.BCrypt

object Security {
  def hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

  def verifyPassword(password: String, hashedPassword: String): Boolean =
    BCrypt.checkpw(password, hashedPassword)
}
