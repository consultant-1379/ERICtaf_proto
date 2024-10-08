
/*global define*/
define('widgets/main',[
    'jscore/core'
], function (jscore) {
    

    var supportedVersions = [
        '1'
    ];
    
    var isCompatible = false;
    
    supportedVersions.forEach(function(version) {
        if (jscore.version.indexOf(version) === 0) {
            isCompatible = true;
        }
    });
    
    if (!isCompatible) {
        throw new Error("Incompatible version of JSCore");
    }
    
    return jscore;

});
