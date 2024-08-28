import com.ericsson.oss.bsim.ui.core.BsimServiceManager;
import com.ericsson.oss.bsim.domain.AddNodeData;
import com.ericsson.oss.bsim.domain.NodeType;

class BsimOsgiOperator{

    final BsimServiceManager BsimServiceManagerInstance = BsimServiceManager.getInstance();
    private AddNodeData data;

    public List<String> getMethods(){
        BsimServiceManagerInstance.getBsimService().metaClass.methods*.name.sort().unique()
    }

    public String createAddNodeData(String nodeName, String btsFdn) {
        this.data = new AddNodeData(nodeName, btsFdn, NodeType.Bts);
        return this.data.dump()
    }

    public def addNode(){
        final List<AddNodeData> nodeDataList = new ArrayList<AddNodeData>();
        nodeDataList.add(data);
        BsimServiceManagerInstance.getBsimService().addNodes(nodeDataList, false,  null);
    }
    




}