package dev.cloudhandson.webmethods.util;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;

public class IDataToolsUtil {
    public static String getLeafNode(IData iData, String nodeName) {
        String[] nodes = nodeName.split("\\.");

        for (int i = 0; i < nodes.length - 1; i++) {
            IDataCursor iDataCursor = iData.getCursor();
            iData = IDataUtil.getIData(iDataCursor, nodes[i]);
            iDataCursor.destroy();
        }

        if (iData == null)
            throw new RuntimeException("IData document not found in the node: " + nodeName);

        return getString(iData, nodes[nodes.length - 1]);
    }

    public static String getString(IData iData, String nodeName) {
        IDataCursor iDataCursor = iData.getCursor();
        String nodeValue = IDataUtil.getString(iDataCursor, nodeName);
        iDataCursor.destroy();

        if (nodeValue == null)
            throw new RuntimeException("IData document not found in the node: " + nodeName);

        return nodeValue;
    }
}