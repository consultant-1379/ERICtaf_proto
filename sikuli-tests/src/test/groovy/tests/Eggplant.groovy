package tests;

if (imagefound(600, "cex_header")) {

    click "cex_header"

    if (imagefound("cex_restore")) {  //eeipls
        click "cex_restore"
    }

    // Don't continue until all cell information has been retrieved
    set "scriptlogging", "off"
    while (imagefound("cex/workbench_early_startup")) {
        wait 3
        if (imagefound("cex/no_response", "cex/error_encountered_retrieving_topology")) {
            LogWarning "Cex Error no response within 120s"
            while (imagefound("cex/cex_ok")) {
                click "cex/cex_ok"
            }
        }
    }

}
