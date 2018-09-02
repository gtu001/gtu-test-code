package gtu._work.mvn;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class LoadPomListDependency {

    public static void main(String[] args) throws MalformedURLException, DocumentException {
        LoadPomListDependency pomReader = new LoadPomListDependency();
        pomReader.pomFile = new File("C:/workspace_RS430/sris-rl/sris-rl-web/pom.xml");
        pomReader.execute();
        System.out.println("done...");
    }

    File pomFile;

    void execute() throws MalformedURLException, DocumentException {
        Document doc = new SAXReader().read(pomFile);
        Element root = doc.getRootElement();
        Pom pom = new Pom(root);
        showSelectNodes("", root);
        System.out.println(pom);
    }

    static String getNodeText(Node node) {
        if (node != null) {
            return node.getText().trim();
        }
        return null;
    }

    static void showSelectNodes(String prefix, Node node) {
        for (Object n2 : node.selectNodes("*")) {
            Node n22 = (Node) n2;
            System.out.println(prefix + n22.getName() + " " + n22.getPath());
            showSelectNodes(prefix + "\t", n22);
        }
    }

    static class Pom implements Serializable {
        private static final long serialVersionUID = -5334027532218102159L;
        final String modelVersion;
        final Pom parent;
        final String groupId;
        final String artifactId;
        final String version;
        final String packaging;
        final String name;
        final String url;
        final String description;
        final List<Dependency> dependencies;

        Pom(Node node) {
            modelVersion = getNodeText(node.selectSingleNode("*[name()='modelVersion']"));
            Node parentNode = node.selectSingleNode("*[name()='parent']");
            if (parentNode != null) {
                parent = new Pom(parentNode);
            } else {
                parent = null;
            }

            groupId = getNodeText(node.selectSingleNode("*[name()='groupId']"));
            artifactId = getNodeText(node.selectSingleNode("*[name()='artifactId']"));
            version = getNodeText(node.selectSingleNode("*[name()='version']"));
            packaging = getNodeText(node.selectSingleNode("*[name()='packaging']"));
            name = getNodeText(node.selectSingleNode("*[name()='name']"));
            url = getNodeText(node.selectSingleNode("*[name()='url']"));
            description = getNodeText(node.selectSingleNode("*[name()='description']"));
            List<Dependency> list = new ArrayList<Dependency>();
            Dependency depen = null;
            for (Object n1 : node.selectNodes("*[name()='dependencies']/*")) {
                depen = new Dependency((Node) n1);
                if (depen != null) {
                    list.add(depen);
                }
            }
            dependencies = list;
        }

        @Override
        public String toString() {
            return "Pom [modelVersion=" + modelVersion + ", groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version + ", packaging=" + packaging + ", name=" + name + ", url="
                    + url + ", description=" + description + ", parent=" + parent + ", dependencies=" + dependencies + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
            result = prime * result + ((dependencies == null) ? 0 : dependencies.hashCode());
            result = prime * result + ((description == null) ? 0 : description.hashCode());
            result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
            result = prime * result + ((modelVersion == null) ? 0 : modelVersion.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((packaging == null) ? 0 : packaging.hashCode());
            result = prime * result + ((parent == null) ? 0 : parent.hashCode());
            result = prime * result + ((url == null) ? 0 : url.hashCode());
            result = prime * result + ((version == null) ? 0 : version.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pom other = (Pom) obj;
            if (artifactId == null) {
                if (other.artifactId != null)
                    return false;
            } else if (!artifactId.equals(other.artifactId))
                return false;
            if (dependencies == null) {
                if (other.dependencies != null)
                    return false;
            } else if (!dependencies.equals(other.dependencies))
                return false;
            if (description == null) {
                if (other.description != null)
                    return false;
            } else if (!description.equals(other.description))
                return false;
            if (groupId == null) {
                if (other.groupId != null)
                    return false;
            } else if (!groupId.equals(other.groupId))
                return false;
            if (modelVersion == null) {
                if (other.modelVersion != null)
                    return false;
            } else if (!modelVersion.equals(other.modelVersion))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (packaging == null) {
                if (other.packaging != null)
                    return false;
            } else if (!packaging.equals(other.packaging))
                return false;
            if (parent == null) {
                if (other.parent != null)
                    return false;
            } else if (!parent.equals(other.parent))
                return false;
            if (url == null) {
                if (other.url != null)
                    return false;
            } else if (!url.equals(other.url))
                return false;
            if (version == null) {
                if (other.version != null)
                    return false;
            } else if (!version.equals(other.version))
                return false;
            return true;
        }
    }

    static class Dependency  implements Serializable {
        private static final long serialVersionUID = -6281321128633980613L;
        final String groupId;
        final String artifactId;
        final String version;
        final String scope;

        Dependency(Node node) {
            groupId = getNodeText(node.selectSingleNode("*[name()='groupId']"));
            artifactId = getNodeText(node.selectSingleNode("*[name()='artifactId']"));
            version = getNodeText(node.selectSingleNode("*[name()='version']"));
            scope = getNodeText(node.selectSingleNode("*[name()='scope']"));
        }

        @Override
        public String toString() {
            return "Dependency [groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version + ", scope=" + scope + "]";
        }
    }

    static class DependencyKey  implements Serializable {
        private static final long serialVersionUID = -7462137407851316257L;
        final String groupId;
        final String artifactId;

        public DependencyKey(String groupId, String artifactId) {
            super();
            this.groupId = groupId;
            this.artifactId = artifactId;
        }

        @Override
        public String toString() {
            return "DependencyKey [groupId=" + groupId + ", artifactId=" + artifactId + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
            result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DependencyKey other = (DependencyKey) obj;
            if (artifactId == null) {
                if (other.artifactId != null)
                    return false;
            } else if (!artifactId.equals(other.artifactId))
                return false;
            if (groupId == null) {
                if (other.groupId != null)
                    return false;
            } else if (!groupId.equals(other.groupId))
                return false;
            return true;
        }
    }
}
