import styles from "./styles.module.css";

interface ContentProps {
  title: string;
  children: React.ReactNode;
  showingRestaurants: boolean;
  clearSelection: () => void;
}

export default function Content({
  title,
  children,
  showingRestaurants,
  clearSelection,
}: ContentProps) {
  return (
    <>
      <div className={styles.title}>
        <span>{title}</span>
        {!showingRestaurants && (
          <button onClick={clearSelection}>
            <span>Clear</span>
          </button>
        )}
      </div>

      <div className={styles.content}>{children}</div>
    </>
  );
}
