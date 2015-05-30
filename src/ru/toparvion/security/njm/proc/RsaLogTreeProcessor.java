package ru.toparvion.security.njm.proc;

import ru.toparvion.security.njm.LogTree;
import ru.toparvion.security.njm.NssFileEntry;
import ru.toparvion.security.njm.TokenNotFoundException;

/**
 * Log tree processor for extracting NSS file pieces required in RSA encryption mode.
 */
public class RsaLogTreeProcessor implements LogTreeProcessor {

  public static final String MODE = "RSA";

  @Override
  public NssFileEntry process(LogTree logTree) {

    String encPreMaster = logTree
            .get("ClientKeyExchange", "[write] MD5 and SHA1 hashes", "0000")
            .orElseThrow(() -> new TokenNotFoundException("No encrypted PreMaster Secret found in the log."))
            .substring(12, 28);

    String decPreMaster = logTree
            .get("ClientKeyExchange", "PreMaster Secret", "0000", "0010", "0020")
            .orElseThrow(() -> new TokenNotFoundException("No PreMaster Secret found in the log."));

    return new NssFileEntry(MODE, encPreMaster, decPreMaster);

  }
}